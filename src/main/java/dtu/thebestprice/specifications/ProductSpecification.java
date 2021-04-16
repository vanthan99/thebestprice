package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.repositories.CategoryRepository;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;
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
//            return criteriaBuilder.equal(root.join("category").get("id"),categoryId);
        };
    }

    public static Specification<Product> retailerIdIn(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (ids == null)
                return criteriaBuilder.conjunction();
            return root.join("productRetailer").get("retailer").get("id").in(ids);
        };
    }
//    public static Specification<Product> orDerBy(boolean priceUp){
//        return (root, criteriaQuery, criteriaBuilder) -> {
//            if (priceUp)
//                return criteriaQuery.orderBy(root.join("").get("prices").)
//        }
//    }
}
