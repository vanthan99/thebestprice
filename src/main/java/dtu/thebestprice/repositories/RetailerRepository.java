package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, Long>, JpaSpecificationExecutor<Retailer> {
    Retailer findByHomePageAndDeleteFlgFalse(String homePage);

    Retailer findByNameAndDeleteFlgFalse(String name);

    Retailer findByDeleteFlgFalseAndHomePage(String homepage);

    // Danh sách nhà bán lẽ đang hoạt động
    Set<Retailer> findByDeleteFlgFalse();


    // danh sách nhà bán lẽ đang hoạt động
    List<Retailer> findByDeleteFlgAndEnableAndApprove(boolean deleteFlg, boolean enable, boolean approve);

    List<Retailer> findByDeleteFlgAndEnable(boolean deleteFlg, boolean enable);

    List<Retailer> findByDeleteFlgFalseAndEnableAndUser(boolean enable, User user);

    List<Retailer> findByDeleteFlgFalseAndUser(User user);

    // Danh sách nhà bán lẽ đã bị xóa
    Set<Retailer> findByDeleteFlgTrue();

    boolean existsByHomePageAndDeleteFlgFalse(String homePage);

    boolean existsByLogoImageAndDeleteFlgFalse(String logoImage);

    boolean existsByNameAndDeleteFlgFalse(String name);

    boolean existsByIdAndNameAndDescriptionAndLogoImageAndHomePageAndUserAndDeleteFlgFalse(
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
    @Query("select r from Retailer r where r.deleteFlg = false and r.approve = :approve and lower(r.name) like concat('%',lower(:keyword) ,'%') ")
    Page<Retailer> findByDeleteFlgFalseAndApproveAndKeyword(boolean approve, String keyword, Pageable pageable);


}
