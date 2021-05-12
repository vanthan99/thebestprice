package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.BannerRequest;
import org.springframework.http.ResponseEntity;

public interface BannerService {
    ResponseEntity<Object> createNew(BannerRequest request);
    
    ResponseEntity<Object> findByEnable(boolean enable);

    ResponseEntity<Object> update(long bannerId, BannerRequest request);

    ResponseEntity<Object> deleteById(long bannerId);

    ResponseEntity<Object> switchEnable(long bannerId);
}
