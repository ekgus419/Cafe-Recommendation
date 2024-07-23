package cafe.recommendation.direction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 고객이 입력한 주소를 입력받을 DTO
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class InputDto {
    private String address;
}