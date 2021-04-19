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
    public Page<LongProductResponse> filter(FilterRequest filterRequest, Pageable pageable) throws Exception {

        Specification specification = Specification
                .where(
                        ProductSpecification.categoryIs(getSetCatId(filterRequest.getCatId()))
                                .and(ProductSpecification.titleContaining(filterRequest.getKeyword()))
                        .and(ProductSpecification.retailerIdIn(convertListRetailerId(filterRequest.getRetailerIds())))

                );

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    @Override
    public Page<LongProductResponse> findByCategoryId(Pageable pageable, String catId) throws Exception  {
        Set<Long> setIds = getSetCatId(catId);
        if (setIds == null) throw new Exception("Mã danh mục trống");
        Specification specification = Specification.where(
                ProductSpecification.categoryIs(setIds)
        );
        Page<Product> productPage = productRepository.findAll(specification,pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    private Set<Long> convertListRetailerId(List<String> retailerIds) throws Exception {
        if (retailerIds == null) return null;
        Set<Long> set = new HashSet<>();
        try {
            retailerIds.forEach(s -> {
                set.add(Long.parseLong(s));
            });
        }catch (Exception e){
            throw new NumberFormatException("Mã nhà bán lẽ không hợp lệ");
        }
        return set;
    }

    private Set<Long> getSetCatId(String categoryId) throws Exception {
        if (categoryId == null) return null;
        Set<Long> longSet = new HashSet<>();
        Category category;
        try {
            category= categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
        }catch (Exception e){
            throw new Exception("Mã danh mục không đúng định dạng");
        }

        if (category != null) {
            longSet.add(category.getId());
            if (category.getCategory() == null) {
                longSet.addAll(categoryRepository.findAllCatIdOfParent(category.getId()));
            }
        }
        else throw new Exception("Không tồn tại id danh mục");
        return longSet;
    }
}
