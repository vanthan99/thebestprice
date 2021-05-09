package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ViewCountConverter;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.payload.response.StatisticViewCountResponse;
import dtu.thebestprice.payload.response.query.ViewCountModel;
import dtu.thebestprice.repositories.ViewCountStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViewCountStatisticServiceImpl implements dtu.thebestprice.services.ViewCountStatisticService {
    @Autowired
    EntityManager entityManager;

    @Autowired
    ViewCountStatisticRepository viewCountStatisticRepository;

    @Autowired
    ViewCountConverter viewCountConverter;

    @Override
    @Transactional
    public ResponseEntity<Object> statisticBetweenDay(LocalDate startDay, LocalDate endDay, Pageable pageable) {
        Query query = entityManager.createQuery(
                "select new dtu.thebestprice.payload.response.query.ViewCountModel(s.viewCount, s.product) " +
                        "from ViewCountStatistic s " +
                        "where s.statisticDay between ?1 and ?2 " +
                        "group by s.product " +
                        "order by s.viewCount desc"
        )
                .setParameter(1, startDay)
                .setParameter(2, endDay);
        return getResult(query, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticByQuarter(int year, int quarter, Pageable pageable) {
        Query query;


        switch (quarter) {
            case 1:
                query = entityManager
                        .createQuery("select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 1 and 3 " +
                                "group by s.product " +
                                "order by viewcount desc"
                        ).setParameter(1, year);
                break;
            case 2:
                query = entityManager
                        .createQuery("select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 4 and 6 " +
                                "group by s.product " +
                                "order by viewcount desc"
                        ).setParameter(1, year);
                break;
            case 3:
                query = entityManager
                        .createQuery("select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between 7 and 9 " +
                                "group by s.product " +
                                "order by viewcount desc"
                        ).setParameter(1, year);
                break;
            case 4:
                query = entityManager
                        .createQuery("select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                                "from ViewCountStatistic s " +
                                "where YEAR(s.statisticDay) = ?1 AND MONTH(s.statisticDay) between ?10 and ?12 " +
                                "group by s.product " +
                                "order by viewcount desc"
                        ).setParameter(1, year);
                break;

            default:
                throw new RuntimeException("Quý nhập vào không hợp lệ. quý là số nguyên. có giá trị từ 1 cho tới 4");
        }

        return getResult(query, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> statisticBetweenYear(int startYear, int endYear, Pageable pageable) {
        Query query = entityManager.createQuery(
                "select new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                        "from ViewCountStatistic s " +
                        "where YEAR(s.statisticDay) between ?1 and ?2 " +
                        "group by s.product " +
                        "order by viewcount desc"
        )
                .setParameter(1, startYear)
                .setParameter(2, endYear);
        return getResult(query, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> top20ProductViewedMostByDateDay(LocalDate nowDay) {
        Query query = entityManager.createQuery(
                "select new dtu.thebestprice.payload.response.query.ViewCountModel(s.viewCount, s.product) " +
                        "from ViewCountStatistic s " +
                        "where s.statisticDay = ?1  " +
                        "group by s.product " +
                        "order by s.viewCount desc"
        )
                .setParameter(1, nowDay);

        List<ViewCountModel> list = query.getResultList();

        List<StatisticViewCountResponse> result = list.stream().map(viewCountModel -> viewCountConverter.toStatisticViewCountResponse(viewCountModel)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Object> statisticBetweenDay(LocalDate startDay, LocalDate endDay) {
        Period different = Period.between(startDay, endDay);

        if (endDay.isAfter(LocalDate.now()))
            throw new RuntimeException("Ngày kết thúc chỉ được thống kê tới hôm nay " + LocalDate.now().toString());

        List<Long> result = new ArrayList<>();

        for (int i = 0; i <= different.getDays(); i++) {
            result.add(viewCountStatisticRepository.countByStatisticDay(startDay.plusDays(i)));
        }

        result = result.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Transactional
    public ResponseEntity<Object> getResult(Query query, Pageable pageable) {

        int totalElements = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        PageCustom page = new PageCustom();

        List<ViewCountModel> list = query.getResultList();


        page.setContent(list.stream().map(viewCountModel -> viewCountConverter.toStatisticViewCountResponse(viewCountModel)).collect(Collectors.toList()));
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
}
