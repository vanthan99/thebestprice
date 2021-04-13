package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRetailerRepository extends JpaRepository<ProductRetailer, Long> {
    List<ProductRetailer> findByProductAndDeleteFlgFalse(Product product);
}
