package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductRetailerService {
    ResponseEntity<Object> toggleEnable(long productRetailerId);

    // danh sách product retailer chưa approve
    ResponseEntity<Object> findByApprove(boolean approve,Pageable pageable);
}
