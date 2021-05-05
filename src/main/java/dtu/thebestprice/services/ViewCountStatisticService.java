package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ViewCountStatisticService {
    ResponseEntity<Object> statisticBetweenDay(LocalDate startDay, LocalDate endDay, Pageable pageable);

    ResponseEntity<Object> statisticBetweenYear(int startYear, int endYear, Pageable pageable);

    ResponseEntity<Object> top20ProductViewedMostByDateDay(LocalDate nowDay);
}
