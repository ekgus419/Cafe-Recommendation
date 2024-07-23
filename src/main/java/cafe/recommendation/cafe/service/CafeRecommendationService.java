package cafe.recommendation.cafe.service;

import cafe.recommendation.api.dto.DocumentDto;
import cafe.recommendation.api.dto.KakaoApiResponseDto;
import cafe.recommendation.api.service.KakaoAddressSearchService;
import cafe.recommendation.direction.dto.OutputDto;
import cafe.recommendation.direction.entity.Direction;
import cafe.recommendation.direction.service.Base62Service;
import cafe.recommendation.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    @Value("${cafe.recommendation.base.url}")
    private String baseUrl;


    public List<OutputDto> recommendCafeList(String address) {

        // 문자열 기반 주소 정보를 위치 기반 데이터로 변경
        // 카카오에서 제공해주는 주소 검색 api를 이용
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[CafeRecommendationService recommendCafeList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        // 위치 기반 데이터 기준으로 가까운 카페를 찾는다.
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // kakao 카테고리를 이용한 장소 검색 api 이용
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        log.info(directionList.toString());
        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction) {
        return OutputDto.builder()
                .cafeName(direction.getTargetCafeName())
                .cafeAddress(direction.getTargetAddress())
                // 고객에게 shortened url 로 제공
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
