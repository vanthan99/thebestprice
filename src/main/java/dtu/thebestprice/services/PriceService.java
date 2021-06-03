package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.price.PriceRequest;
import dtu.thebestprice.payload.request.price.PriceRetailerRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PriceService {

    // retailer cập nhật giá
    ResponseEntity<Object> retailerUpdatePrice(long productRetailerId, PriceRequest priceRequest);

    // admin phê duyệt giá
    ResponseEntity<Object> adminApprovePrice(long productRetailerId);

    // admin cập nhật giá cho mọi sản phẩm
    ResponseEntity<Object> adminUpdatePrice(long productRetailerId, PriceRequest priceRequest);

    // admin lấy danh sách giá theo sản phẩm
    ResponseEntity<Object> adminGetPriceByProduct(long productId);

    // retailer tạo mới 1 price
    ResponseEntity<Object> retailerCreateNewPrice(long productId, PriceRetailerRequest priceRetailerRequest);

    // retailer xóa product_retailer
    ResponseEntity<Object> retailerDelete(long productRetailerId);

    // amdin thêm mới giá
    ResponseEntity<Object> adminCreateNewPrice(long productId, PriceRetailerRequest priceRetailerRequest);

    // admin xoa gia
    ResponseEntity<Object> adminDelete(long productRetailerId);

    // retailer get page giá những sản phẩm của mình
    ResponseEntity<Object> getPagePriceForRetailer(Pageable pageable);
}
