package dtu.thebestprice.services;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<LongProductResponse> filter(String keyword, Long categoryId, Pageable pageable);
}
