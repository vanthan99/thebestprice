package dtu.thebestprice.services.Impl;

import dtu.thebestprice.controllers.ProductController;
import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.query.ViewCountModel;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.HotDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotDealServiceImpl implements HotDealService {

    @Autowired
    ProductConverter productConverter;

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public ResponseEntity<Object> top10Laptop() {
        LocalDate nowDay = LocalDate.now();
        return getResultByCategory(21L, nowDay.getYear(), nowDay.getMonthValue());
    }

    @Override
    @Transactional
    public ResponseEntity<Object> top10Smartphone() {
        LocalDate nowDay = LocalDate.now();
        return getResultByCategory(1L, nowDay.getYear(), nowDay.getMonthValue());
    }

    @Override
    @Transactional
    public ResponseEntity<Object> top10Product() {
        LocalDate nowDate = LocalDate.now();

        int year = nowDate.getYear();
        int month = nowDate.getMonthValue();

        Query query = entityManager
                .createQuery(
                        "select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where YEAR(s.statisticDay) = ?1 " +
                                "and MONTH(s.statisticDay) = ?2 " +
                                "group by s.product " +
                                "order by viewcount desc"
                )
                .setParameter(1, year)
                .setParameter(2, month)
                .setMaxResults(10);


        List<ViewCountModel> list = query.getResultList();

        List<LongProductResponse> result = list.stream().map(viewCountModel -> productConverter.toLongProductResponse(viewCountModel.getProduct())).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Transactional
    public ResponseEntity<Object> getResultByCategory(Long catId, int year, int month) {
        Query query = entityManager
                .createQuery(
                        "select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where s.product.category.category.id = ?3  " +
                                "and YEAR(s.statisticDay) = ?1 " +
                                "and MONTH(s.statisticDay) = ?2 " +
                                "group by s.product " +
                                "order by viewcount desc"
                )
                .setParameter(1, year)
                .setParameter(2, month)
                .setParameter(3, catId)
                .setMaxResults(10);

        List<ViewCountModel> list = query.getResultList();

        List<LongProductResponse> result = list.stream().map(viewCountModel -> productConverter.toLongProductResponse(viewCountModel.getProduct())).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
