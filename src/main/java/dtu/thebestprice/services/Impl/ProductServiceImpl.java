package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;
    
    @Override
    public Page<LongProductResponse> filter(String keyword, Long categoryId, Pageable pageable) {
        Specification specification = Specification.where(ProductSpecification.categoryIs(categoryId).and(ProductSpecification.titleContaining(keyword)));
//        return productRepository.findAll(specification,pageable);

        Page<Product> productPage = productRepository.findAll(specification,pageable);
        return  productPage.map(product -> productConverter.toLongProductResponse(product));
    }
}
