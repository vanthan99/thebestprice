package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.price.ProductRetailerRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductRetailerService {
    ResponseEntity<Object> toggleEnable(long productRetailerId);

    // danh sách product retailer chưa approve
    ResponseEntity<Object> findByApprove(boolean approve,Pageable pageable);

    // admin hay chủ chỉnh sửa
    ResponseEntity<Object> update(long productRetailerId, ProductRetailerRequest productRetailerRequest);
}
