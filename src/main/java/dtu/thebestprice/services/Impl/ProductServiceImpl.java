package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.*;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.request.product.ProductFullRequest;
import dtu.thebestprice.payload.response.*;
import dtu.thebestprice.payload.response.query.ViewCountModel;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.specifications.ProductSpecification;
import dtu.thebestprice.specifications.ViewCountStatisticSpecification;
import org.hibernate.service.NullServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import javax.naming.AuthenticationNotSupportedException;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

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
    public ResponseEntity<Object> findById(long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("id của sản phẩm không tồn tại"));

        if (!product.isEnable()) {
            // kiểm tra chủ sản phẩm
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                    //when Anonymous Authentication is enabled
                    !(SecurityContextHolder.getContext().getAuthentication()
                            instanceof AnonymousAuthenticationToken)) {

                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByUsername(username).orElse(null);

                if ((product.getCreatedBy().equals(username) || user.getRole().equals(ERole.ROLE_ADMIN)) && !product.isApprove())
                    return ResponseEntity.ok(productConverter.toLongProductResponse(product));
            }
            throw new RuntimeException("Sản phẩm không hoạt động");
        }

        return ResponseEntity.ok(productConverter.toLongProductResponse(product));
    }


    @Override
    public ResponseEntity<Object> create(ProductRequest productRequest) {
        long categoryId;
        try {
            categoryId = Long.parseLong(productRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("categoryId phải là số nguyên");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh mục phải là danh mục con");
        Product product = productConverter.toEntity(productRequest);
        product.setEnable(true);
        product.setApprove(true);

        productRepository.save(product);

        // save hình ảnh
        List<String> images = productRequest.getImages();
        if (images != null && images.size() >= 3) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Chỉnh sửa thất bại. hệ thống không biết bạn là ai"));

        long categoryId;
        try {
            categoryId = Long.parseLong(productRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("categoryId phải là số nguyên");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh mục phải là danh mục con");

        Product currentProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("id sản phẩm không tồn tại"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !currentProduct.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("Không thể chỉnh sửa sản phẩm này. bạn không phải là người tạo ra sản phẩm");

        Product newProduct = productConverter.toEntity(productRequest, currentProduct);

        // nếu mà admin cập nhật thì set approve và enable là true
        if (user.getRole().equals(ERole.ROLE_ADMIN)) {
            newProduct.setEnable(true);
            newProduct.setApprove(true);
        } else {
            newProduct.setApprove(false);
        }

        productRepository.save(newProduct);


        // xóa những hình ảnh trước đó
        List<Image> imageListInEntity = imageRepository.findByProductAndDeleteFlgFalse(newProduct);
        if (imageListInEntity.size() > 0) {
            // xóa những image hết
            imageListInEntity.forEach(image -> {
                image.setDeleteFlg(true);
                imageRepository.save(image);
            });
        }

        // save image
        List<String> images = productRequest.getImages();
        if (images != null && images.size() >= 3) {
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

        if (product.isDeleteFlg())
            throw new RuntimeException("Sản phẩm này đã bị xóa ra khỏi hệ thống trước đó");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Hệ thống không biết bạn là ai"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !product.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("Bạn không thể xóa sản phẩm này vì không đủ điều kiện");

        product.setDeleteFlg(true);

        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa sản phẩm thành công"));
    }

    @Override
    public ResponseEntity<Object> findByApprove(boolean b, String keyword, Pageable pageable) {
        Specification<Product> condition = Specification.where(
                ProductSpecification.titleContaining(keyword)
                        .or(ProductSpecification.shortDescContaining(keyword))
                        .or(ProductSpecification.longDescContaining(keyword)))
                .and(ProductSpecification.isApprove(b))
                .and(ProductSpecification.deleteFlgFalse());

        Page<Product> entityPage = productRepository.findAll(condition, pageable);

        Page<ShortProductResponse> responsePage = entityPage.map(product -> productConverter.toShortProductResponse(product));

        return ResponseEntity.ok(responsePage);
//        Page<ShortProductResponse> page;
//        if (keyword.trim().equals(""))
//            page = productRepository
//                    .findByApproveAndDeleteFlgFalse(b, pageable)
//                    .map(product -> productConverter.toShortProductResponse(product));
//        else
//            page = productRepository
//                    .findByApproveAndDeleteFlgFalseAndKeyword(b, keyword, pageable)
//                    .map(product -> productConverter.toShortProductResponse(product));
//
//        return ResponseEntity.ok(page);
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
                            "WHERE s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and lower(s.product.title) like concat('%',lower(?2) ,'%') and YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?3 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword)
                    .setParameter(3, month);

        } else if (keyword != null && year == null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?1) ,'%') " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, keyword);

        } else if (keyword != null && year != null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?2) ,'%') and YEAR(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword);

        } else if (keyword == null && year != null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and   YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?2 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, month);
        } else if (keyword == null && year == null && month == null) {
            // trả về tất cả
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product ) " +
                            "FROM ViewCountStatistic s where s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ");
        } else if (keyword == null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where  s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and  month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month);
        } else if (keyword != null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?2) ,'%') and month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month)
                    .setParameter(2, keyword);
        } else {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where  s.product.deleteFlg = false and s.product.enable = true and s.product.approve = true and  year(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year);
        }

        return getResult(query, pageable);
    }

    @Override
    public ResponseEntity<Object> toggleEnable(long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại sản phẩm"));

        String message = "";
        if (product.isEnable())
            message = "Tắt trạng thái hoạt động của sản phẩm thành công";
        else message = "Bật trạng thái hoạt động của sản phẩm thành công";

        product.setEnable(!product.isEnable());
        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminApprove(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        if (product.isApprove())
            throw new RuntimeException("Sản phẩm đã được phê duyệt trước đó");

        product.setApprove(true);
        product.setEnable(true);
        productRepository.save(product);
        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt sản phẩm thành công"));
    }

    @Override
    public ResponseEntity<Object> retailerCreateProduct(ProductFullRequest productFullRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long retailerId;
        try {
            retailerId = Long.parseLong(productFullRequest.getRetailerId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("retailer Id phải là số nguyên");
        }

        Retailer retailer = retailerRepository.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại retailer này"));

        if (!authentication.getName().equals(retailer.getUser().getUsername()))
            throw new RuntimeException("Bạn không phải chủ của nhà bán lẽ này");


        long price;
        try {
            price = Long.parseLong(productFullRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Giá phải là số nguyên");
        }

        long categoryId;
        try {
            categoryId = Long.parseLong(productFullRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("categoryId phải là số nguyên");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh mục phải là danh mục con");
        Product product = productConverter.toEntity(productFullRequest);
        product.setEnable(false);
        product.setApprove(false);
        productRepository.save(product);

        // save hình ảnh
        List<String> images = productFullRequest.getImages();
        if (images != null && images.size() >= 3) {
            images.forEach(imageItem -> {
                if (!imageItem.trim().equalsIgnoreCase("")) {
                    saveImage(product, imageItem);
                }
            });
        }


        // tạo mới product_retailer
        ProductRetailer productRetailer = new ProductRetailer(productFullRequest.getUrl(), retailer, product, false, false);
        productRetailerRepository.save(productRetailer);

        priceRepository.save(new Price(price, productRetailer));

        return ResponseEntity.ok(new ApiResponse(true, "Yêu cầu thêm mới sản phẩm thành công.Hãy đợi quản trị viên phê duyệt"));
    }

    @Override
    public ResponseEntity<Object> findProductById(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("không tồn tại sản phẩm"));

        if (product.isDeleteFlg())
            throw new RuntimeException("Sản phẩm này đã bị xóa khỏi hệ thống");

        if (!product.isEnable())
            throw new RuntimeException("Sản phẩm này đang bị khóa");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("hệ thống không thể nhận biết được bạn"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !product.getCreatedBy().equals(authentication))
            throw new RuntimeException("Bạn không đủ quyền để xem thông tin sản phẩm này");

        return ResponseEntity.ok(productConverter.toProductResponse(product));
    }

    @Override
    public ResponseEntity<Object> listProductForRetailer(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Page<Product> productPage = productRepository.findByDeleteFlgFalseAndCreatedBy(authentication.getName(), pageable);
        Page<ShortProductResponse> page = productPage.map(product -> productConverter.toShortProductResponse(product));
        return ResponseEntity.ok(page);
    }

    @Transactional
    public ResponseEntity<Object> getResult(Query query, Pageable pageable) {
        int totalElements = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        PageCustom page = new PageCustom();

        List<ViewCountModel> list = query.getResultList();

        List<ShortProductResponse> result = list.stream().map(viewCountModel -> productConverter.toShortProductResponse(viewCountModel.getProduct())).collect(Collectors.toList());

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
