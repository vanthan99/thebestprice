package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameAndDeleteFlgFalse(String username);

    boolean existsByPhoneNumberAndDeleteFlgFalse(String phoneNumber);

    boolean existsByUsernameAndDeleteFlgFalse(String username);

    boolean existsByEmailAndDeleteFlgFalse(String email);

    // danh sách tài khoản đã phê duyệt hay chưa phê duyệt sắp xếp theo ngày
    Page<User> findByDeleteFlgFalseAndEnableTrueAndApproveFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findByDeleteFlgFalseAndRole(Pageable pageable, ERole role);

    @Query("select u from User u where u.deleteFlg = false and u.role = :role and lower(u.username) like concat('%',lower(:keyword) ,'%') ")
    Page<User> findByDeleteFlgFalseAndRoleAndKeyword(Pageable pageable, ERole role, String keyword);

    Long countByRoleAndEnableTrueAndApproveTrueAndDeleteFlgFalse(ERole role);

    // page người dùng theo role
    Page<User> findByDeleteFlgFalseAndEnableTrueAndApproveTrueAndRole(Pageable pageable, ERole role);

    boolean existsByIdAndFullNameAndAddressAndPhoneNumberAndDeleteFlgFalse(Long id, String fullName, String address, String phoneNumber);

    boolean existsByIdAndFullNameAndAddressAndPhoneNumberAndUsernameAndEmailAndDeleteFlgFalse(Long id, String fullName, String address, String phoneNumber, String username, String email);
}
