package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // danh sách tài khoản chưa phê duyệt sắp xếp theo ngày
    Page<User> findByDeleteFlgFalseAndEnableTrueAndApproveFalseOrderByCreatedAtDesc(Pageable pageable);
}
