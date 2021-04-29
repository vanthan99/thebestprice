package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface ProductTrackingService {
    ResponseEntity<Object> productTracking(Long productId);
}
