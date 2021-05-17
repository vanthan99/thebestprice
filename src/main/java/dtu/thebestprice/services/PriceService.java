package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.price.RetailerUpdatePriceRequest;
import org.springframework.http.ResponseEntity;

public interface PriceService {

    // retailer cập nhật giá
    ResponseEntity<Object> retailerUpdatePrice(RetailerUpdatePriceRequest priceRequest);

    // admin phê duyệt giá
    ResponseEntity<Object> adminApprovePrice(long priceId);

    // admin cập nhật giá cho mọi sản phẩm
    ResponseEntity<Object> adminUpdatePrice(RetailerUpdatePriceRequest priceRequest);

    // admin lấy danh sách giá theo sản phẩm
    ResponseEntity<Object> adminGetPriceByProduct(long productId);
}
