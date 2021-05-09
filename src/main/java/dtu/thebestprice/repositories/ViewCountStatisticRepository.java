package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ViewCountStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ViewCountStatisticRepository extends JpaRepository<ViewCountStatistic, Long> {
    ViewCountStatistic findByStatisticDay(LocalDate date);

    ViewCountStatistic findByStatisticDayAndProduct(LocalDate date, Product product);

    boolean existsByStatisticDay(LocalDate date);

    boolean existsByStatisticDayAndProduct(LocalDate date, Product product);

    @Query("select s from ViewCountStatistic s where s.statisticDay between :startDay and :endDay order by s.viewCount desc")
    Page<ViewCountStatistic> statisticViewCountProductByBetweenDay(LocalDate startDay, LocalDate endDay, Pageable pageable);

    // lấy số lượt xem theo ngày
    @Query("select sum(v.viewCount) from ViewCountStatistic v where v.statisticDay = :date")
    Long countByStatisticDay(LocalDate date);
}
