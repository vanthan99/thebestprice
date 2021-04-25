package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    Retailer findByHomePage(String homePage);

    Retailer findByName(String name);

    // Danh sách nhà bán lẽ đang hoạt động
    Set<Retailer> findByDeleteFlgFalse();

    // Danh sách nhà bán lẽ đã bị xóa
    Set<Retailer> findByDeleteFlgTrue();

    boolean existsByHomePage(String homePage);

    boolean existsByLogoImage(String logoImage);

    boolean existsByName(String name);
}
