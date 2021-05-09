package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByTitleAndCategory(String title, Category category);

    // Danh sách danh mục cha đang hoạt động
    List<Category> findByCategoryIsNullAndDeleteFlgFalseOrderByCreatedAtDesc();

    // danh sách các danh mục con có cùng 1 danh mục cha truyền vào
    List<Category> findByCategoryAndDeleteFlgFalseOrderByCreatedAtDesc(Category parentCategory);

    Category findByTitle(String title);

    // chuyển delete flag về true khi khuyển vào category id
    @Transactional
    @Modifying
    @Query("update Category c set c.deleteFlg = true where c.category.id = :parentId")
    void deleteChildCategoryByParentId(Long parentId);

    // kiểm tra id truyền vào có phải là danh mục con hay không?
    boolean existsByIdAndCategoryIsNotNull(Long id);

    /*
     * Kiểm tra id truyền vào có phải là danh mục cha hay khôgn */
    boolean existsByIdAndCategoryIsNull(Long id);

    /*
     * Kiểm tra danh sách con có tồn tại titleInput hay chưa?
     * */
    boolean existsByTitleAndCategory(String titleInput, Category category);

    /*
     * Kiểm tra xem danh sách danh mục cha có tồn tại titleInput hay chưa?
     * */
    boolean existsByTitleAndCategoryIsNull(String titleInput);

    /*
     * Tìm danh mục cha có Id truyền vào và đang hoạt động
     * */
    Category findByIdAndCategoryIsNull(Long categoryId);

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
