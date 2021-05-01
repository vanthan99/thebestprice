package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

public interface StatisticService {
    // Thống kê tìm kiếm theo 1 ngày truyền vào
    ResponseEntity<Object> statisticSearchBySingleDay(LocalDate date);

    ResponseEntity<Object> statisticSearchByMonth(int month, int year);

    ResponseEntity<Object> statisticSearchByYear(int year);
}
