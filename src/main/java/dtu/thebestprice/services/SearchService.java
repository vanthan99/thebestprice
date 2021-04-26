package dtu.thebestprice.services;


import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.PageCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    PageCustom filter(FilterRequest filterRequest, Pageable pageable);
}
