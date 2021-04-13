package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByTitleAndCategory(String title,Category category);
    Category findByTitleAndCategory(String title,Category category);
    Category findByTitle(String title);

    // Lấy danh sách category có category truyền vào là parent.
    List<Category> findByOrderByCategory();
//    List<Category>
}
