package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.ProductRetailer;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findFirstByProductRetailerOrderByUpdatedAtDesc(ProductRetailer productRetailer);

    @Query("select p.price from Price p where p.productRetailer = :productRetailer and p.active = true ")
    Long findByPriceLatestByProductRetailer(ProductRetailer productRetailer);

    Price findByProductRetailerAndActive(ProductRetailer productRetailer, boolean active);
}
