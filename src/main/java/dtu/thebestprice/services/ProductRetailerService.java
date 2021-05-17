package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface ProductRetailerService {
    ResponseEntity<Object> toggleEnable(long productRetailerId);
}
