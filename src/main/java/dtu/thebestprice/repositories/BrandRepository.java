package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Brand;
import dtu.thebestprice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);

    Optional<Brand> findByDeleteFlgFalseAndIdAndEnableTrue(Long brandId);

    boolean existsByName(String name);

    List<Brand> findByDeleteFlgFalseAndEnable(boolean enable);
}
