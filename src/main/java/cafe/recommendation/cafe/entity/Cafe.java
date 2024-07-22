package cafe.recommendation.cafe.entity;

import cafe.recommendation.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Cafe")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cafe extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cafeName;
    private String cafeAddress;
    private double latitude;
    private double longitude;

    public void changeCafeAddress(String address) {
        this.cafeAddress = address;
    }
}