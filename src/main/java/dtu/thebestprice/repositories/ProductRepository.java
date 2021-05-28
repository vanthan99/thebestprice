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

    Page<Product> findByApproveAndDeleteFlgFalse(boolean approve, Pageable pageable);

    @Query("select p from Product p where p.deleteFlg = false and p.approve = :approve and lower(p.title) like concat('%',lower(:keyword) ,'%') ")
    Page<Product> findByApproveAndDeleteFlgFalseAndKeyword(boolean approve, String keyword, Pageable pageable);

    Page<Product> findByDeleteFlgFalseAndCreatedBy(String createdBy, Pageable pageable);

    boolean existsByCode(String code);

    Product findByCode(String code);
}
