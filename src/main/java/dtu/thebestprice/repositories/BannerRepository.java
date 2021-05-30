package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    // danh sách baner đang hoạt động
    List<Banner> findByDeleteFlgFalseAndEnableOrderByCreatedAtDesc(boolean enable);

    Page<Banner> findByDeleteFlgFalse(Pageable pageable);

    Optional<Banner> findByDeleteFlgFalseAndId(Long id);
}
