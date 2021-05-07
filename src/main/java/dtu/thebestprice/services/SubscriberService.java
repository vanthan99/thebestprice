package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.SubscriberRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SubscriberService {
    ResponseEntity<Object> create(SubscriberRequest request);

    ResponseEntity<Object> findAll(Pageable pageable);
}
