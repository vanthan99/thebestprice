package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatisticDay;
import dtu.thebestprice.entities.SearchStatisticYearMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface SearchStatisticYearMonthRepository extends JpaRepository<SearchStatisticYearMonth, Long> {

    boolean existsBySearchAndStatisticMonthAndStatisticYear(Search search,int month, int year);


    // danh sách 10 từ khóa được tìm kiếm nhiều nhất khi truyền vào ngày/tháng/năm thống kê
    List<SearchStatisticYearMonth> findFirst10ByStatisticMonthAndStatisticYearOrderByTotalOfSearchDesc(int month, int year);

    @Modifying
    @Transactional
    @Query("UPDATE SearchStatisticYearMonth p SET p.totalOfSearch = (p.totalOfSearch + 1)" +
            " WHERE p.search.id = :searchId AND p.statisticMonth = :month AND p.statisticYear = :year")
    void updateTotalOfSearch(Long searchId, int month, int year);
}
