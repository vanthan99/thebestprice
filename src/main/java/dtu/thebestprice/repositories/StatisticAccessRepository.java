package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.StatisticAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface StatisticAccessRepository extends JpaRepository<StatisticAccess, Long> {
    boolean existsByAuthAndStatisticDay(boolean auth, LocalDate date);

    StatisticAccess findByAuthAndStatisticDay(boolean auth, LocalDate date);

    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where year(s.statisticDay) = :year " +
            " and month(s.statisticDay) = :month ")
    Long countByMonth(int year, int month);
}
