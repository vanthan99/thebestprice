package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    Retailer findByHomePage(String homePage);

    Retailer findByName(String name);

    // Danh sách nhà bán lẽ đang hoạt động
    Set<Retailer> findByDeleteFlgFalse();


    // danh sách nhà bán lẽ đang hoạt động
    List<Retailer> findByDeleteFlgAndEnableAndApprove(boolean deleteFlg, boolean enable, boolean approve);

    List<Retailer> findByDeleteFlgAndEnable(boolean deleteFlg, boolean enable);

    // Danh sách nhà bán lẽ đã bị xóa
    Set<Retailer> findByDeleteFlgTrue();

    boolean existsByHomePage(String homePage);

    boolean existsByLogoImage(String logoImage);

    boolean existsByName(String name);

    boolean existsByIdAndNameAndDescriptionAndLogoImageAndHomePageAndUser(
            Long id,
            String name,
            String description,
            String logoImage,
            String homePage,
            User user
    );

    // page retailer chưa được phê duyệt hoặc đã được phê duyệt
    Page<Retailer> findByDeleteFlgFalseAndApprove(boolean approve, Pageable pageable);

    // page retailer chưa được phê duyệt hoặc đã được phê duyệt sắp xếp giảm giầm theo thời gian
    Page<Retailer> findByDeleteFlgFalseAndApproveOrderByCreatedAt(boolean approve, Pageable pageable);


}
