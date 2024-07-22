package cafe.recommendation.cafe.repository;

import cafe.recommendation.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
}
