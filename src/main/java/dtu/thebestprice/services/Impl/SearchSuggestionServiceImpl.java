package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.response.ProductItem;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchSuggestionService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchSuggestionServiceImpl implements SearchSuggestionService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Override
    public Page<ProductItem> findByKeyword(String keyword,Pageable pageable) {
        Specification condition = Specification.where(
                ProductSpecification.titleContaining(keyword)
        );
        Page<Product> productPage = productRepository.findAll(condition,pageable);
        return productPage.map(product -> productConverter.toProductItem(product));
    }
}
