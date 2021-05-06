package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface SearchStatisticService {
    // Thống kê tìm kiếm theo 1 ngày truyền vào
//    ResponseEntity<Object> statisticSearchBySingleDay(LocalDate date);

//    ResponseEntity<Object> statisticSearchByMonth(int month, int year);

//    ResponseEntity<Object> statisticSearchByYear(int year);

    // top 10 từ khóa được tìm kiếm nhiều nhất ngày
    ResponseEntity<Object> top10KeywordMostSearched(LocalDate statisticDay);

    // thống kê từ khóa theo ngày
    // truyền vào ngày bắt đầu và ngày kế thúc
    ResponseEntity<Object> statisticDateBetween(LocalDate startDay, LocalDate endDay, Pageable pageable);

    // thống kê từ khóa theo tháng
    ResponseEntity<Object> statisticMonthBetween(int startMonth, int startYear, int endMonth, int endYear, Pageable pageable);

    // thống kê từ khóa theo quý
    ResponseEntity<Object> statisticByQuarter(int quarter, int year, Pageable pageable);

    // thống kê từ khóa theo năm
    ResponseEntity<Object> statisticYearBetween(int startYear, int endYear, Pageable pageable);

    // thống kê số lượt tìm kiếm theo ngày
    ResponseEntity<Object> countSearchByBetweenDay(LocalDate startDay, LocalDate endDay);

    // thống kê số lượt tìm kiếm theo quý
    ResponseEntity<Object> countSearchByQuarter(int quarter, int year);

    // thống kê số lượt tìm kiếm theo năm
    ResponseEntity<Object> countSearchByBetweenYear(int startYear, int endYear);


}
