package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.request.product.ProductFullRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable) throws Exception;

    Page<LongProductResponse> findByCategoryId(Pageable pageable, String catId) throws Exception;

    ResponseEntity<Object> findById(long productId);

    ResponseEntity<Object> create(ProductRequest productRequest);

    ResponseEntity<Object> update(ProductRequest productRequest, Long productId);

    ResponseEntity<Object> deleteById(Long id);

    // page sản phẩm đã phê duyệt hoặc chưa phê duyệt
    ResponseEntity<Object> findByApprove(boolean b,String keyword, Pageable pageable);

    // page sản phẩm được xem nhiều nhất tháng hiện tại
    ResponseEntity<Object> pageProductMostViewMonth(String keyword, Pageable pageable, Integer month, Integer year);

    ResponseEntity<Object> toggleEnable(long productId);

    // admin approve sản phẩm
    ResponseEntity<Object> adminApprove(long productId);

    // retailer đăng sản phẩm (kèm giá và url tới nơi bán)
    ResponseEntity<Object> retailerCreateProduct(ProductFullRequest productFullRequest);

    // admin, retailer tìm sản phẩm
    ResponseEntity<Object> findProductById(long productId);

    // danh sách sản phẩm của role retailer
    ResponseEntity<Object> listProductForRetailer(Pageable pageable);

    // page sản phẩm thuộc retailer sở hữu được xem nhiều nhất
    ResponseEntity<Object> pageProductMostViewMonthForRetailer(String keyword, Pageable pageable, Integer month, Integer year);
}
