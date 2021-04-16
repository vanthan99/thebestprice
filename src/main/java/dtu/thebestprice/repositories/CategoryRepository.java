package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByTitleAndCategory(String title,Category category);
    Category findByTitleAndCategory(String title,Category category);
    Category findByTitle(String title);

    /*
     * Lấy tất cả category id con khi truyền vào id của cat cha.
     * */
    @Query("SELECT c.id FROM Category c WHERE c.category.id = :id")
    Set<Long> findAllCatIdOfParent(Long id);

    List<Category> findByCategoryIsNull();

    // Lấy danh sách category có category truyền vào là parent.
    List<Category> findByOrderByCategory();
//    List<Category>
}
