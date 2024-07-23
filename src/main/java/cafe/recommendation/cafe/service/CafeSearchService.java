package cafe.recommendation.cafe.service;

import cafe.recommendation.cafe.dto.CafeDto;
import cafe.recommendation.cafe.entity.Cafe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeSearchService {

    private final CafeRepositoryService cafeRepositoryService;

    public List<CafeDto> searchCafeDtoList() {

        return cafeRepositoryService.findAll()
                .stream()
                .map(this::convertToCafeDto)
                .collect(Collectors.toList());
    }

    private CafeDto convertToCafeDto(Cafe cafe) {
        // 엔티티를 dto 로 변환해서 사용.
        // 엔티티를 수정한다는 것은 데이터베이스를 변경하는것과 마찬가지로 봄.
        // 따라서 컨트롤러 단이나 뷰단 등에서 사용시에는 DTO로 담아서 넘긴다.
        return CafeDto.builder()
                .id(cafe.getId())
                .cafeAddress(cafe.getCafeAddress())
                .cafeName(cafe.getCafeName())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .build();
    }
}