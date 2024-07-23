package cafe.recommendation.direction.dto;

import lombok.Builder;
import lombok.Getter;

// 고객이 입력한 주소 기준으로 거리 계산 알고리즘을 통해서
// 가까운 카페를 찾고 결과물을 리턴 해줄 DTO
@Getter
@Builder
public class OutputDto {

    private String cafeName;     // 카페 명
    private String cafeAddress;  // 카페 주소
    private String directionUrl; // 길안내 url
    private String roadViewUrl;  // 로드뷰 url
    private String distance;     // 고객 주소와 카페 주소의 거리
}
