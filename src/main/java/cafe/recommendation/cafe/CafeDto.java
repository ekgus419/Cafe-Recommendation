package cafe.recommendation.cafe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeDto {
    private Long id;
    private String cafeName;
    private String cafeAddress;
    private double latitude;
    private double longitude;
}
