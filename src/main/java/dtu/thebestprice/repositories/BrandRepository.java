package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);

    boolean existsByName(String name);

    List<Brand> findByDeleteFlgFalseAndEnable(boolean enable);
}
