package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.ProductRetailer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Long> {
    Price findFirstByProductRetailerOrderByUpdatedAtDesc(ProductRetailer productRetailer);
}
