package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable) throws Exception;

    Page<LongProductResponse> findByCategoryId(Pageable pageable, String catId) throws Exception;

    LongProductResponse findById(String productId) throws Exception;

    ResponseEntity<Object> create(ProductRequest productRequest);

    ResponseEntity<Object> update(ProductRequest productRequest, Long productId);

    ResponseEntity<Object> deleteById(Long id);
}
