package dtu.thebestprice.repositories;

import com.fasterxml.jackson.databind.node.LongNode;
import dtu.thebestprice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /*
     * Update view count
     * set view count + 1
     * */
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.viewCount =(p.viewCount + 1) WHERE p.id = :productId")
    void updateViewCount(Long productId);
}
