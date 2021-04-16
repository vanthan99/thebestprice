package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    Retailer findByHomePage(String homePage);

    Retailer findByName(String name);
}
