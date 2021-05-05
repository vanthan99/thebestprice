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
}
