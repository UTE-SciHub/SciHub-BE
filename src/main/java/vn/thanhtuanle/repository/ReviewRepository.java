package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
