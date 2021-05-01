package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.SearchTrackingRequest;
import org.springframework.http.ResponseEntity;

public interface SearchTrackingService {
    ResponseEntity<Object> searchTracking(SearchTrackingRequest searchRequest);
}
