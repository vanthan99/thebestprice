package dtu.thebestprice.services;

import dtu.thebestprice.payload.response.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchSuggestionService {
    Page<ProductItem> findByKeyword(String keyword, Pageable pageable);
}
