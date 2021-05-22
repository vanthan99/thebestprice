package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {
    public static Specification<User> userNameContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<User> fullNameContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<User> addressContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<User> emailContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<User> phoneNumberContaining(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().equals(""))
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<User> deleteFlgFalse() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("deleteFlg"));
    }

    public static Specification<User> isRole(ERole role) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role);
    }
}
