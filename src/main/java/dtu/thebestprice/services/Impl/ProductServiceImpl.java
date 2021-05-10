package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Category;
import dtu.thebestprice.entities.Image;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.payload.response.SearchResponse;
import dtu.thebestprice.payload.response.query.ViewCountModel;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.repositories.ImageRepository;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.hibernate.service.NullServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    EntityManager entityManager;

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
    public Page<LongProductResponse> findByCategoryId(Pageable pageable, String catId) throws Exception {
        Set<Long> setIds = getSetCatId(catId);
        if (setIds == null) throw new Exception("Mã danh mục trống");
        Specification specification = Specification.where(
                ProductSpecification.categoryIs(setIds)
        );
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    @Override
    public LongProductResponse findById(String productId) throws Exception {
        long id;
        try {
            id = Long.parseLong(productId);
        } catch (Exception e) {
            throw new Exception("id sản phẩm không đúng định dạng");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new Exception("id của sản phẩm không tồn tại"));

        return productConverter.toLongProductResponse(product);
    }


    @Override
    public ResponseEntity<Object> create(ProductRequest productRequest) {
        if (categoryRepository.existsByIdAndCategoryIsNull(productRequest.getCategoryId()))
            throw new RuntimeException("id danh mục phải là danh mục con");
        Product product = productConverter.toEntity(productRequest);

        productRepository.save(product);

        // save hình ảnh
        List<String> images = productRequest.getImages();
        if (images != null && images.size() > 0) {
            images.forEach(imageItem -> {
                if (!imageItem.trim().equalsIgnoreCase("")) {
                    saveImage(product, imageItem);
                }
            });

        }

        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới sản phẩm thành công"));
    }

    @Override
    public ResponseEntity<Object> update(ProductRequest productRequest, Long productId) {
        if (categoryRepository.existsByIdAndCategoryIsNull(productRequest.getCategoryId()))
            throw new RuntimeException("id danh mục phải là danh mục con");

        Product currentProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("id sản phẩm không tồn tại"));

        Product newProduct = productConverter.toEntity(productRequest, currentProduct);

        productRepository.save(newProduct);

        // save image
        List<String> images = productRequest.getImages();
        if (images != null && images.size() > 0) {
            images.forEach(imageUrl -> {

                /*
                 * Kiểm tra nếu sản phẩm đã có hình ảnh này thì bỏ qua
                 * nếu sản phẩm chưa có hình ảnh này thì thêm vào
                 * */
                if (!imageRepository.existsByProductAndUrlAndDeleteFlgFalse(newProduct, imageUrl)) {
                    saveImage(newProduct, imageUrl);
                }
            });
        }
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật sản phẩm thành công"));
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id sản phẩm không tồn tại"));

        product.setDeleteFlg(true);

        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa sản phẩm thành công"));
    }

    @Override
    public ResponseEntity<Object> findByApprove(boolean b, Pageable pageable) {

        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> pageProductMostViewMonth(String keyword, Pageable pageable, Integer month, Integer year) {
        Query query;
        if (keyword != null && year != null && month != null) {
            // trả về tất cả
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.title like concat('%',?2,'%') and YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?3 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword)
                    .setParameter(3, month);

        } else if (keyword != null && year == null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.title like concat('%',?1,'%') " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, keyword);

        } else if (keyword != null && year != null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.title like concat('%',?2,'%') and YEAR(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword);

        } else if (keyword == null && year != null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE  YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?2 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, month);
        } else if (keyword == null && year == null && month == null) {
            // trả về tất cả
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ");
        } else if (keyword == null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month);
        } else if (keyword != null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.title like concat('%',?2,'%') and month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month)
                    .setParameter(2, keyword);
        } else {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where year(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year);
        }

        return getResult(query, pageable);
    }

    @Transactional
    public ResponseEntity<Object> getResult(Query query, Pageable pageable) {
        int totalElements = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        PageCustom page = new PageCustom();

        List<ViewCountModel> list = query.getResultList();

        List<LongProductResponse> result = list.stream().map(viewCountModel -> productConverter.toLongProductResponse(viewCountModel.getProduct())).collect(Collectors.toList());

        page.setContent(result);
        page.setTotalElements(totalElements);
        page.setSize(pageable.getPageSize());
        page.setNumber(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) totalElements / page.getSize()));
        page.setFirst(page.getNumber() == 0);
        page.setLast(page.getTotalPages() - 1 == page.getNumber());
        page.setEmpty(page.getContent().size() == 0);
        page.setNumberOfElements(page.getContent().size());

        return ResponseEntity.ok(page);
    }

    private void saveImage(Product product, String imageItem) {
        Image image = new Image();

        image.setProduct(product);
        image.setUrl(imageItem.trim());

        imageRepository.save(image);
    }

    private Set<Long> convertListRetailerId(List<String> retailerIds) throws Exception {
        if (retailerIds == null) return null;
        Set<Long> set = new HashSet<>();
        try {
            retailerIds.forEach(s -> {
                set.add(Long.parseLong(s));
            });
        } catch (Exception e) {
            throw new NumberFormatException("Mã nhà bán lẽ không hợp lệ");
        }
        return set;
    }

    private Set<Long> getSetCatId(String categoryId) throws Exception {
        if (categoryId == null) return null;
        Set<Long> longSet = new HashSet<>();
        Category category;
        try {
            category = categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
        } catch (Exception e) {
            throw new Exception("Mã danh mục không đúng định dạng");
        }

        if (category != null) {
            longSet.add(category.getId());
            if (category.getCategory() == null) {
                longSet.addAll(categoryRepository.findAllCatIdOfParent(category.getId()));
            }
        } else throw new Exception("Không tồn tại id danh mục");
        return longSet;
    }
}
