package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

public interface ViewCountStatisticService {
    ResponseEntity<Object> statisticBetweenDay(LocalDate startDay, LocalDate endDay, Pageable pageable);

    ResponseEntity<Object> statisticByQuarter(int year, int quarter, Pageable pageable);

    ResponseEntity<Object> statisticBetweenYear(int startYear, int endYear, Pageable pageable);

    ResponseEntity<Object> top20ProductViewedMostByDateDay(LocalDate nowDay);

    // thống kê lượt xem sản phẩm theo ngày bắt đầu và ngày kết thúc.
    // đếm số lượt xem của sản phẩm theo ngày.
    ResponseEntity<Object> statisticBetweenDay(Date startDay, Date endDay);
}
