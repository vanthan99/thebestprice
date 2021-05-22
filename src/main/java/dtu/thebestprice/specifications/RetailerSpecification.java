package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.Retailer;
import org.springframework.data.jpa.domain.Specification;

public final class RetailerSpecification {
    public static Specification<Retailer> nameContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Retailer> descContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Retailer> deleteFlgFalse() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("deleteFlg"));
    }

    public static Specification<Retailer> isApprove(boolean approve) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("approve"), approve);
    }

}
