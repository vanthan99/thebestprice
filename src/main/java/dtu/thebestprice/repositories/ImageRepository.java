package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Image;
import dtu.thebestprice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByProductAndDeleteFlgFalse(Product product);
}
