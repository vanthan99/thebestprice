package dtu.thebestprice.services;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable) throws Exception;
    Page<LongProductResponse> findByCategoryId(Pageable pageable,String catId) throws Exception;
    LongProductResponse findById(String productId) throws Exception;
}
