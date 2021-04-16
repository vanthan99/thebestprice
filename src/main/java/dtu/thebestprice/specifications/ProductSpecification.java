package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public final class ProductSpecification {
    @Autowired
    CategoryRepository categoryRepository;

    public static Specification<Product> titleContaining(String title) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (title == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
        };
    }

    public static Specification<Product> categoryIs(Set<Long> categoryIds) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (categoryIds == null)
                return criteriaBuilder.conjunction();
            return root.join("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Product> retailerIdIn(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (ids == null)
                return criteriaBuilder.conjunction();
            return root.join("productRetailer").get("retailer").get("id").in(ids);
        };
    }
}
