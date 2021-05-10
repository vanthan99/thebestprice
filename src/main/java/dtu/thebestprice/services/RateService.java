package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface RateService {
    ResponseEntity<Object> rating(Long userId, Long rate, Long productId);
}
