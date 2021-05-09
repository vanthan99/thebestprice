package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Retailer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // page retailer chưa được phê duyệt hoặc đã được phê duyệt
    Page<Retailer> findByDeleteFlgFalseAndApprove(boolean approve, Pageable pageable);

    // page retailer chưa được phê duyệt hoặc đã được phê duyệt sắp xếp giảm giầm theo thời gian
    Page<Retailer> findByDeleteFlgFalseAndApproveOrderByCreatedAt(boolean approve, Pageable pageable);
}
