package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface StatisticAccessService {
    ResponseEntity<Object> save(boolean auth);
}
