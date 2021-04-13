package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Role;
import dtu.thebestprice.entities.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
