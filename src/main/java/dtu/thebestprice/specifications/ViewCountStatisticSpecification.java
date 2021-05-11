package dtu.thebestprice.specifications;

import dtu.thebestprice.entities.ViewCountStatistic;
import org.springframework.data.jpa.domain.Specification;

public final class ViewCountStatisticSpecification {

    public static Specification<ViewCountStatistic> likeKeyword(String keyword) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (keyword == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.join("product").get("title")), '%' + keyword.toLowerCase() + '%');
        };
    }

    public static Specification<ViewCountStatistic> isMonth(Integer month){
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (month == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(criteriaBuilder.function("MONTH",Integer.class,root.get("statisticDay")),month);
        };
    }

    public static Specification<ViewCountStatistic> isYear(Integer year){
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (year == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(criteriaBuilder.function("YEAR",Integer.class,root.get("statisticDay")),year);
        };
    }
}
