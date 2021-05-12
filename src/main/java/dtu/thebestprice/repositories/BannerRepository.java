package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner,Long> {
    // danh sách baner đang hoạt động
    List<Banner> findByDeleteFlgFalseAndEnableOrderByCreatedAtDesc(boolean enable);
}
