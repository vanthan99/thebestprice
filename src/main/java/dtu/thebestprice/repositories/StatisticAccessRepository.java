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

    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where s.auth = true and year(s.statisticDay) = :year " +
            " and month(s.statisticDay) = :month ")
    Long countByMonthAndAuthTrue(int year, int month);

    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where s.auth = false and year(s.statisticDay) = :year " +
            " and month(s.statisticDay) = :month ")
    Long countByMonthAndAuthFalse(int year, int month);


    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where year(s.statisticDay) = :year " +
            " and month(s.statisticDay) in :months ")
    Long countByQuarter(int year, int[] months);

    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where s.auth = false and year(s.statisticDay) = :year ")
    Long countByYearAndAuthFalse(int year);

    @Query("select sum(s.counter)" +
            " from StatisticAccess s" +
            " where s.auth = true and year(s.statisticDay) = :year ")
    Long countByYearAndAuthTrue(int year);


}
