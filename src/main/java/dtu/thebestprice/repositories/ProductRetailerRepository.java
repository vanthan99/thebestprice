package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRetailerRepository extends JpaRepository<ProductRetailer, Long> {
    boolean existsByProductAndRetailer(Product product, Retailer retailer);

    boolean existsByProductAndRetailerAndDeleteFlgFalse(Product product, Retailer retailer);

    List<ProductRetailer> findByDeleteFlgFalseAndApproveTrueAndRetailer(Retailer retailer);

    List<ProductRetailer> findByProductAndDeleteFlgFalse(Product product);

    Page<ProductRetailer> findByDeleteFlgFalseAndApprove(boolean approve, Pageable pageable);

    List<ProductRetailer> findByDeleteFlgFalseAndProduct(Product product);

    List<ProductRetailer> findByDeleteFlgFalseAndEnableAndApproveAndProduct(boolean enable, boolean approve, Product product);
}
