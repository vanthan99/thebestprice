package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

}
