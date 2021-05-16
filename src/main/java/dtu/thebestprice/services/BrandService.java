package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface BrandService {

    // danh sách tất cả brand đang hoạt động
    ResponseEntity<Object> findAllBrandIsEnable();
}
