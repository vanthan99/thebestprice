package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;

public interface SearchStatisticRepository extends JpaRepository<SearchStatistic, Long> {
    boolean existsByStatisticDayAndSearch(LocalDate statisticDay, Search search);

    @Modifying
    @Transactional
    @Query("UPDATE SearchStatistic s SET s.numberOfSearch = (s.numberOfSearch + 1)" +
            " WHERE s.search.id = :searchId AND s.statisticDay = :statisticDay ")
    void updateNumberOfSearch(Long searchId, LocalDate statisticDay);

    // đếm số lượt tìm kiếm theo tháng
    @Query("select sum(s.numberOfSearch)" +
            " from SearchStatistic s " +
            "where year(s.statisticDay) = :year" +
            " and month(s.statisticDay) = :month ")
    Long countByMonth(int year, int month);

    // đếm số lượt tìm kiếm theo quý
    @Query("select sum(s.numberOfSearch)" +
            " from SearchStatistic s " +
            "where year(s.statisticDay) = :year" +
            " and month(s.statisticDay) in :months ")
    Long countByQuarter(int year, int[] months);

    // tính tổng số lần tìm kiếm theo từ khóa
    @Query("select sum(s.numberOfSearch) from SearchStatistic s where s.search = :search")
    Long countBySearch(Search search);

    // đếm tổng số lần tìm kiếm theo ngày
    @Query("select sum(s.numberOfSearch) from SearchStatistic s where s.statisticDay = :date")
    Long countByDay(LocalDate date);
}
