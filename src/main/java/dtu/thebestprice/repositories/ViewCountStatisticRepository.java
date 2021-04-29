package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ViewCountStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ViewCountStatisticRepository extends JpaRepository<ViewCountStatistic, Long> {
    ViewCountStatistic findByStatisticDay(LocalDate date);

    ViewCountStatistic findByStatisticDayAndProduct(LocalDate date, Product product);

    boolean existsByStatisticDay(LocalDate date);

    boolean existsByStatisticDayAndProduct(LocalDate date, Product product);
}
