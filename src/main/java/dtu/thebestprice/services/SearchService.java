package dtu.thebestprice.services;


import dtu.thebestprice.payload.response.LongProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {
    Page<LongProductResponse> filter(String keyword);
}
