package dtu.thebestprice.services;

import dtu.thebestprice.payload.response.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchSuggestionService {
    Page<ProductItem> findByKeyword(String keyword, Pageable pageable);
    List<ProductItem> findByKeywordV2(String keyword);
}
