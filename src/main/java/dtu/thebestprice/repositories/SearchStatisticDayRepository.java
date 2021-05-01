package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatisticDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface SearchStatisticDayRepository extends JpaRepository<SearchStatisticDay, Long> {
    boolean existsBySearchAndStatisticDay(Search search, LocalDate date);

    SearchStatisticDay findBySearchAndStatisticDay(Search search, LocalDate date);

    List<SearchStatisticDay> findByStatisticDay(LocalDate statisticDay);

    // danh sách 10 từ khóa được tìm kiếm nhiều nhất khi truyền vào ngày/tháng/năm thống kê
    List<SearchStatisticDay> findFirst10ByStatisticDayOrderByTotalOfSearchDesc(LocalDate statisticDay);

    // danh sách 10 từ khóa được tìm kiếm nhiều nhất khi truyền vào tháng/năm thống kê
//    @Query(value = "SELECT p FROM SearchStatistic p where MONTH(p.statistic_day) = MONTH(:date)" +
//            " AND YEAR (p.statistic_day) = YEAR (:date)",nativeQuery = true)
//    List<SearchStatistic> findByMonth(LocalDate date);

    @Modifying
    @Transactional
    @Query("UPDATE SearchStatisticDay p SET p.totalOfSearch = (p.totalOfSearch + 1)" +
            " WHERE p.search.id = :searchId AND p.statisticDay = :statisticDay ")
    void updateTotalOfSearch(Long searchId, LocalDate statisticDay);
}
