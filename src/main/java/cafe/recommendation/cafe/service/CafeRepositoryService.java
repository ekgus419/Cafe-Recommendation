package cafe.recommendation.cafe.service;

import cafe.recommendation.cafe.entity.Cafe;
import cafe.recommendation.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeRepositoryService {

    private final CafeRepository cafeRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        Cafe entity = cafeRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[CafeRepositoryService updateAddress] not found id:{}", id);
            return;
        }

        entity.changeCafeAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

}