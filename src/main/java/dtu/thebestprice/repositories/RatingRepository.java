package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    @Query(value = "SELECT AVG(r.rate)  FROM Rating r WHERE r.product.id=:productId")
    Long getRateByProduct(Long productId);
}
