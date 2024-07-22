package cafe.recommendation.cafe.repository

import cafe.recommendation.cafe.entity.Cafe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class CafeRepositoryTest extends Specification {

    @Autowired
    CafeRepository cafeRepository = Mock()

    def setup() {
        cafeRepository.deleteAll()
    }


    def "CafeRepository save"() {
        given:
        String address = "경기 부천시 소사구 소사본동"
        String name = "테스트 카페"
        double latitude = 36.22
        double longitude = 128.33

        def cafe = Cafe.builder()
                .cafeAddress(address)
                .cafeName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = cafeRepository.save(cafe)

        then:
        result.getCafeName() == name
        result.getCafeAddress() == address
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "CafeRepository saveAll"() {
        given:
        String address = "서울 특별시 강서구 화곡동"
        String name = "테스트 카페"
        double latitude = 36.11
        double longitude = 128.11

        def cafe = Cafe.builder()
                .cafeAddress(address)
                .cafeName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        cafeRepository.saveAll(Arrays.asList(cafe))
        def result = cafeRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "서울 특별시 강서구 화곡동"
        String name = "테스트 카페"

        def cafe = Cafe.builder()
                .cafeAddress(address)
                .cafeName(name)
                .build()

        when:
        cafeRepository.save(cafe)
        def result = cafeRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)

    }
}
