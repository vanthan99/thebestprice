package dtu.thebestprice.services;

import org.springframework.http.ResponseEntity;

public interface HotDealService {
    // top 10 laptop được xem nhiều nhất
    ResponseEntity<Object> top10Laptop();

    // top 10 dien thoai được xem nhiều nhất
    ResponseEntity<Object> top10Smartphone();

    // top 10 sản phẩm đươc xem nhiều nhất tháng
    ResponseEntity<Object> top10Product();
}
