package cafe.recommendation.direction.service;


import cafe.recommendation.api.dto.DocumentDto;
import cafe.recommendation.api.service.KakaoCategorySearchService;
import cafe.recommendation.cafe.service.CafeSearchService;
import cafe.recommendation.direction.entity.Direction;
import cafe.recommendation.direction.repository.DirectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    // 카페 최대 검색 갯수
    private static final int MAX_SEARCH_COUNT = 3;
    // 반경 10Km
    private static final double RADIUS_KM = 10.0;

    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final CafeSearchService cafeSearchService;

    private final KakaoCategorySearchService kakaoCategorySearchService;

    private final DirectionRepository directionRepository;

    private final Base62Service base62Service;


    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if (CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodedId) {
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        // 카페명, 위도, 경도 각 컴마로 구분자지어 추가
        String params = String.join(",",
                direction.getTargetCafeName(),
                String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        return result;
    }

    public List<Direction> buildDirectionList(DocumentDto documentDto) {
        if (Objects.isNull(documentDto)) return Collections.emptyList();
        // 고객의 주소와 카페의 주소를 비교
        return  cafeSearchService.searchCafeDtoList()
                .stream().map(cafeDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetCafeName(cafeDto.getCafeName())
                                .targetAddress(cafeDto.getCafeAddress())
                                .targetLatitude(cafeDto.getLatitude())
                                .targetLongitude(cafeDto.getLongitude())
                                .distance(
                                        calculateDistance(
                                            documentDto.getLatitude(), documentDto.getLongitude(),
                                                cafeDto.getLatitude(), cafeDto.getLongitude()
                                        )
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // cafe search by category kakao api
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestCafeCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetCafeName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}