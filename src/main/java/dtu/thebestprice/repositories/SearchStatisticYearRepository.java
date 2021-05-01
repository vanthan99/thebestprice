package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatisticDay;
import dtu.thebestprice.entities.SearchStatisticYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface SearchStatisticYearRepository extends JpaRepository<SearchStatisticYear,Long> {
    boolean existsBySearchAndStatisticYear(Search search, int year);


    // danh sách 10 từ khóa được tìm kiếm nhiều nhất khi truyền vào ngày/tháng/năm thống kê
    List<SearchStatisticYear> findFirst10ByStatisticYearOrderByTotalOfSearchDesc(int statisticYear);

    @Modifying
    @Transactional
    @Query("UPDATE SearchStatisticYear p SET p.totalOfSearch = (p.totalOfSearch + 1)" +
            " WHERE p.search.id = :searchId AND p.statisticYear = :statisticYear ")
    void updateTotalOfSearch(Long searchId, int statisticYear);
}

