package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable);
}
