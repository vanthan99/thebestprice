package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.SetJoin;
import java.util.Set;

public final class ProductSpecification {
    @Autowired
    CategoryRepository categoryRepository;

    public static Specification<Product> titleContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> deleteFlgFalse() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("deleteFlg"));
    }

    public static Specification<Product> shortDescContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("shortDescription")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> longDescContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("longDescription")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> isApprove(boolean approve) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("approve"), approve);
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
            SetJoin<Product, ProductRetailer> setJoin = root.joinSet("productRetailers");
            return setJoin.join("retailer").get("id").in(ids);
        };
    }
}
