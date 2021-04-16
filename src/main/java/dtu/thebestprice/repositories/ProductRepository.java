package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {



//    @Query("SELECT p FROM Product p, ProductRetailer pr,Retailer r, Category c WHERE pr.product.id = p.id and pr.retailer.id = r.id and p.category.id = c.id  " +
//            "AND (:keyword IS NULL OR lower(p.title) LIKE CONCAT('%',lower(:keyword) ,'%'))" +
//            "AND (:catIds IS NULL OR (c.id IN (:catIds)))" +
//            "AND (:retailerIds IS NULL OR (r.id IN (:retailerIds)))")
//    Page<Product> filterV1(
//            String keyword,
//            Set<Long> catIds,
//            Set<Long> retailerIds,
//            Pageable pageable
//            );
}
