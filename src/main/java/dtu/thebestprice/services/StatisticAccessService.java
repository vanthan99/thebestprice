package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface StatisticAccessService {
    ResponseEntity<Object> save(boolean auth);

    ResponseEntity<Object> staticBetweenDay(Date startDay, Date endDay);
}
