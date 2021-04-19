package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Category;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.hibernate.service.NullServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable) throws NumberFormatException {

        Specification specification = Specification
                .where(
                        ProductSpecification.categoryIs(getSetCatId(filterRequest.getCatId()))
                                .and(ProductSpecification.titleContaining(filterRequest.getKeyword()))
                        .and(ProductSpecification.retailerIdIn(convertListRetailerId(filterRequest.getRetailerIds())))

                );

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    private Set<Long> convertListRetailerId(List<String> retailerIds) throws  NumberFormatException {
        Set<Long> set = new HashSet<>();
        retailerIds.forEach(s -> {
            set.add(Long.parseLong(s));
        });
        return set;
    }

    private Set<Long> getSetCatId(String categoryId) throws NumberFormatException {
        if (categoryId == null) return null;
        Set<Long> longSet = new HashSet<>();
        Category category = categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
        if (category != null) {
            longSet.add(category.getId());
            if (category.getCategory() == null) {
                longSet.addAll(categoryRepository.findAllCatIdOfParent(category.getId()));
            }
        }
        return longSet;
    }
}
