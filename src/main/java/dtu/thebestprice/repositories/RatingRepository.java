package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.Rating;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.services.RateService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT AVG(r.rate)  FROM Rating r WHERE r.product.id=:productId and r.product.deleteFlg = false")
    Double getRateByProduct(Long productId);

    boolean existsByProductAndUser(Product product, User user);

    Rating findByProductAndUser(Product product, User user);

    @Query("select count(r) from Rating r where r.product = :product")
    Long countByProduct(Product product);
}
