package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {
    public static Specification<Product> titleContaining(String title) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (title == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Product> categoryIs(Long categoryId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (categoryId == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.join("category").get("id"),categoryId);
        };
    }
}
