package dtu.thebestprice.services.Impl;

import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.payload.response.query.StatisticCountSearchByQuarterModel;
import dtu.thebestprice.payload.response.query.StatisticSearchModel;
import dtu.thebestprice.repositories.SearchStatisticRepository;
import dtu.thebestprice.services.SearchStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

;

@Service
public class SearchStatisticServiceImpl implements SearchStatisticService {
    @Autowired
    SearchStatisticRepository searchStatisticRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public ResponseEntity<Object> top10KeywordMostSearched(LocalDate statisticDay) {
        Query query = entityManager
                .createQuery(
                        "SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, s.numberOfSearch )  " +
                                "FROM SearchStatistic s " +
                                "WHERE s.statisticDay = ?1 " +
                                "order by s.numberOfSearch desc ")
                .setParameter(1, statisticDay)
                .setMaxResults(10);
        List<StatisticSearchModel> listResult = query.getResultList();
        return ResponseEntity.ok(listResult);

    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticDateBetween(LocalDate startDay, LocalDate endDay, Pageable pageable) {
        Query query = entityManager
                .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                        "FROM SearchStatistic s " +
                        "WHERE s.statisticDay BETWEEN ?1 AND ?2 " +
                        "GROUP BY s.search " +
                        "ORDER BY number desc ")
                .setParameter(1, startDay)
                .setParameter(2, endDay);

        return getResult(query, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticMonthBetween(int startMonth, int startYear, int endMonth, int endYear, Pageable pageable) {
        Query query = entityManager
                .createQuery("SELECT new dtu.thebestprice.payload.response.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                        "FROM SearchStatistic s " +
                        "WHERE (MONTH(s.statisticDay) between ?1 and ?3) " +
                        "AND (YEAR(s.statisticDay) between ?2 and 4 ) " +
                        "GROUP BY s.search " +
                        "ORDER BY number desc ")
                .setParameter(1, startMonth)
                .setParameter(2, startYear)
                .setParameter(3, endMonth)
                .setParameter(4, endYear);

        return getResult(query, pageable);

    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticByQuarter(int quarter, int year, Pageable pageable) {
        Query query;


        switch (quarter) {
            case 1:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND  MONTH(s.statisticDay) BETWEEN  1 AND 3 " +
                                "GROUP BY s.search " +
                                "ORDER BY number desc "
                        ).setParameter(1, year);
                break;
            case 2:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND  MONTH(s.statisticDay) BETWEEN  4 AND 6 " +
                                "GROUP BY s.search " +
                                "ORDER BY number desc "
                        ).setParameter(1, year);
                break;
            case 3:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND  MONTH(s.statisticDay) BETWEEN  7 AND 9 " +
                                "GROUP BY s.search " +
                                "ORDER BY number desc "
                        ).setParameter(1, year);
                break;
            case 4:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND  MONTH(s.statisticDay) BETWEEN  10 AND 12 " +
                                "GROUP BY s.search " +
                                "ORDER BY number desc "
                        ).setParameter(1, year);
                break;

            default:
                throw new RuntimeException("Quý nhập vào không hợp lệ. quý là số nguyên. có giá trị từ 1 cho tới 4");
        }


        return getResult(query, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticYearBetween(int startYear, int endYear, Pageable pageable) {
        Query query = entityManager
                .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticSearchModel(s.search.keyword, SUM(s.numberOfSearch) AS number)  " +
                        "FROM SearchStatistic s " +
                        "WHERE YEAR(s.statisticDay) BETWEEN  ?1 AND ?2 " +
                        "GROUP BY s.search " +
                        "ORDER BY number desc ")
                .setParameter(1, startYear)
                .setParameter(2, endYear);

        return getResult(query, pageable);
    }

    @Transactional
    public ResponseEntity<Object> getResult(Query query, Pageable pageable) {
        int totalElements = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        PageCustom page = new PageCustom();

        List<StatisticSearchModel> list = query.getResultList();

        page.setContent(Collections.singletonList(list));
        page.setTotalElements(totalElements);
        page.setSize(pageable.getPageSize());
        page.setNumber(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) totalElements / page.getSize()));
        page.setFirst(page.getNumber() == 0);
        page.setLast(page.getTotalPages() - 1 == page.getNumber());
        page.setEmpty(page.getContent().size() == 0);
        page.setNumberOfElements(page.getContent().size());

        return ResponseEntity.ok(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> countSearchByBetweenDay(LocalDate startDay, LocalDate endDay) {
        Query query =
                entityManager.createQuery(
                        "SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchModel(s.statisticDay, sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE s.statisticDay between ?1 and ?2" +
                                "GROUP BY s.statisticDay " +
                                "ORDER BY s.statisticDay"
                )
                        .setParameter(1, startDay)
                        .setParameter(2, endDay);

        return ResponseEntity.ok(query.getResultList());
    }

    @Override
    public ResponseEntity<Object> countSearchByQuarter(int quarter, int year) {

        Query query;
        switch (quarter) {
            case 1:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchByQuarterModel(YEAR(s.statisticDay), 1,sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 1 and 3 " +
                                "GROUP BY YEAR(s.statisticDay) "
                        ).setParameter(1, year);
                break;
            case 2:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchByQuarterModel( YEAR(s.statisticDay), 2,sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 4 and 6 " +
                                "GROUP BY YEAR(s.statisticDay) "
                        ).setParameter(1, year);
                break;
            case 3:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchByQuarterModel(YEAR(s.statisticDay), 3,sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 7 and 9 " +
                                "GROUP BY YEAR(s.statisticDay) "
                        ).setParameter(1, year);
                break;
            case 4:
                query = entityManager
                        .createQuery("SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchByQuarterModel(YEAR(s.statisticDay), 4,sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 10 and 12 " +
                                "GROUP BY YEAR(s.statisticDay) "
                        ).setParameter(1, year);
                break;

            default:
                throw new RuntimeException("Quý nhập vào không hợp lệ. quý là số nguyên. có giá trị từ 1 cho tới 4");
        }
        try {
            return ResponseEntity.ok(query.getSingleResult());

        } catch (Exception e){
            return ResponseEntity.status(404).body(new ApiResponse(false,"Không có dữ liệu cho quý "+ quarter +" Năm "+year));
        }


    }

    @Override
    public ResponseEntity<Object> countSearchByBetweenYear(int startYear, int endYear) {
        Query query =
                entityManager.createQuery(
                        "SELECT new dtu.thebestprice.payload.response.query.StatisticCountSearchByYearModel(YEAR(s.statisticDay) as yearStatistic, sum(s.numberOfSearch)) " +
                                "FROM SearchStatistic s " +
                                "WHERE YEAR(s.statisticDay) between ?1 and ?2" +
                                "GROUP BY YEAR(s.statisticDay) " +
                                "ORDER BY yearStatistic desc"
                )
                        .setParameter(1, startYear)
                        .setParameter(2, endYear);

        return ResponseEntity.ok(query.getResultList());
    }
}