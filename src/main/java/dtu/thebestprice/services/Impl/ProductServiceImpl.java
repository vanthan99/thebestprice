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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
                                .and(ProductSpecification.deleteFlgFalse())
                );

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    @Override
    public Page<LongProductResponse> findByCategoryId(Pageable pageable, String catId) throws Exception {
        Set<Long> setIds = getSetCatId(catId);
        if (setIds == null) throw new Exception("M?? danh m???c tr???ng");
        Specification specification = Specification.where(
                ProductSpecification.categoryIs(setIds)
        ).and(ProductSpecification.deleteFlgFalse());
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> productConverter.toLongProductResponse(product));
    }

    @Override
    public ResponseEntity<Object> findById(long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("id c???a s???n ph???m kh??ng t???n t???i"));

        if (!product.isEnable()) {
            // ki???m tra ch??? s???n ph???m
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                    //when Anonymous Authentication is enabled
                    !(SecurityContextHolder.getContext().getAuthentication()
                            instanceof AnonymousAuthenticationToken)) {

                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByUsernameAndDeleteFlgFalse(username).orElse(null);

                if ((product.getCreatedBy().equals(username) || user.getRole().equals(ERole.ROLE_ADMIN)) && !product.isApprove())
                    return ResponseEntity.ok(productConverter.toLongProductResponse(product));
            }
            throw new RuntimeException("S???n ph???m kh??ng ho???t ?????ng");
        }

        return ResponseEntity.ok(productConverter.toLongProductResponse(product));
    }


    @Override
    public ResponseEntity<Object> create(ProductRequest productRequest) {
        long categoryId;
        try {
            categoryId = Long.parseLong(productRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("categoryId ph???i l?? s??? nguy??n");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh m???c ph???i l?? danh m???c con");
        Product product = productConverter.toEntity(productRequest);
        product.setEnable(true);
        product.setApprove(true);

        productRepository.save(product);

        // save h??nh ???nh
        List<String> images = productRequest.getImages();
        if (images != null && images.size() >= 3) {
            images.forEach(imageItem -> {
                if (!imageItem.trim().equalsIgnoreCase("")) {
                    saveImage(product, imageItem);
                }
            });

        }

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i s???n ph???m th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> update(ProductRequest productRequest, Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsernameAndDeleteFlgFalse(authentication.getName()).orElseThrow(() -> new RuntimeException("Ch???nh s???a th???t b???i. h??? th???ng kh??ng bi???t b???n l?? ai"));

        long categoryId;
        try {
            categoryId = Long.parseLong(productRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("categoryId ph???i l?? s??? nguy??n");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh m???c ph???i l?? danh m???c con");

        Product currentProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("id s???n ph???m kh??ng t???n t???i"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !currentProduct.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("Kh??ng th??? ch???nh s???a s???n ph???m n??y. b???n kh??ng ph???i l?? ng?????i t???o ra s???n ph???m");

        Product newProduct = productConverter.toEntity(productRequest, currentProduct);

        // n???u m?? admin c???p nh???t th?? set approve v?? enable l?? true
        if (user.getRole().equals(ERole.ROLE_ADMIN)) {
            newProduct.setEnable(true);
            newProduct.setApprove(true);
        } else {
            newProduct.setApprove(false);
        }

        productRepository.save(newProduct);


        // x??a nh???ng h??nh ???nh tr?????c ????
        List<Image> imageListInEntity = imageRepository.findByProductAndDeleteFlgFalse(newProduct);
        if (imageListInEntity.size() > 0) {
            // x??a nh???ng image h???t
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
                 * Ki???m tra n???u s???n ph???m ???? c?? h??nh ???nh n??y th?? b??? qua
                 * n???u s???n ph???m ch??a c?? h??nh ???nh n??y th?? th??m v??o
                 * */
                if (!imageRepository.existsByProductAndUrlAndDeleteFlgFalse(newProduct, imageUrl)) {
                    saveImage(newProduct, imageUrl);
                }
            });
        }
        return ResponseEntity.ok(new ApiResponse(true, "C???p nh???t s???n ph???m th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id s???n ph???m kh??ng t???n t???i"));

        if (product.isDeleteFlg())
            throw new RuntimeException("S???n ph???m n??y ???? b??? x??a ra kh???i h??? th???ng tr?????c ????");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsernameAndDeleteFlgFalse(authentication.getName())
                .orElseThrow(() -> new RuntimeException("H??? th???ng kh??ng bi???t b???n l?? ai"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !product.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("B???n kh??ng th??? x??a s???n ph???m n??y v?? kh??ng ????? ??i???u ki???n");

        product.setDeleteFlg(true);

        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, "X??a s???n ph???m th??nh c??ng"));
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
            // tr??? v??? t???t c???
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
            // tr??? v??? t???t c???
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
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i s???n ph???m"));

        String message = "";
        if (product.isEnable())
            message = "T???t tr???ng th??i ho???t ?????ng c???a s???n ph???m th??nh c??ng";
        else message = "B???t tr???ng th??i ho???t ?????ng c???a s???n ph???m th??nh c??ng";

        product.setEnable(!product.isEnable());
        productRepository.save(product);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminApprove(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("S???n ph???m kh??ng t???n t???i"));

        if (product.isApprove())
            throw new RuntimeException("S???n ph???m ???? ???????c ph?? duy???t tr?????c ????");

        product.setApprove(true);
        product.setEnable(true);
        productRepository.save(product);
        return ResponseEntity.ok(new ApiResponse(true, "Ph?? duy???t s???n ph???m th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> retailerCreateProduct(ProductFullRequest productFullRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long retailerId;
        try {
            retailerId = Long.parseLong(productFullRequest.getRetailerId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("retailer Id ph???i l?? s??? nguy??n");
        }

        Retailer retailer = retailerRepository.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i retailer n??y"));

        if (!authentication.getName().equals(retailer.getUser().getUsername()))
            throw new RuntimeException("B???n kh??ng ph???i ch??? c???a nh?? b??n l??? n??y");


        long price;
        try {
            price = Long.parseLong(productFullRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Gi?? ph???i l?? s??? nguy??n");
        }

        if (price < 1000)
            throw new RuntimeException("Gi?? ph???i l?? s??? d????ng v?? kh??ng b?? h??n 1000");

        long categoryId;
        try {
            categoryId = Long.parseLong(productFullRequest.getCategoryId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("categoryId ph???i l?? s??? nguy??n");
        }

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(categoryId))
            throw new RuntimeException("id danh m???c ph???i l?? danh m???c con");

        if (productRetailerRepository.existsByDeleteFlgFalseAndUrl(productFullRequest.getUrl()))
            throw new RuntimeException("URL ???? b??? tr??ng");

        Product product = productConverter.toEntity(productFullRequest);
        product.setEnable(false);
        product.setApprove(false);
        productRepository.save(product);

        // save h??nh ???nh
        List<String> images = productFullRequest.getImages();
        if (images != null && images.size() >= 3) {
            images.forEach(imageItem -> {
                if (!imageItem.trim().equalsIgnoreCase("")) {
                    saveImage(product, imageItem);
                }
            });
        }


        // t???o m???i product_retailer
        ProductRetailer productRetailer = new ProductRetailer(productFullRequest.getUrl(), retailer, product, false, false);
        productRetailerRepository.save(productRetailer);

        priceRepository.save(new Price(price, productRetailer));

        return ResponseEntity.ok(new ApiResponse(true, "Y??u c???u th??m m???i s???n ph???m th??nh c??ng.H??y ?????i qu???n tr??? vi??n ph?? duy???t"));
    }

    @Override
    public ResponseEntity<Object> findProductById(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("kh??ng t???n t???i s???n ph???m"));

        if (product.isDeleteFlg())
            throw new RuntimeException("S???n ph???m n??y ???? b??? x??a kh???i h??? th???ng");

        if (!product.isEnable())
            throw new RuntimeException("S???n ph???m n??y ??ang b??? kh??a");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsernameAndDeleteFlgFalse(authentication.getName())
                .orElseThrow(() -> new RuntimeException("h??? th???ng kh??ng th??? nh???n bi???t ???????c b???n"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !product.getCreatedBy().equals(authentication))
            throw new RuntimeException("B???n kh??ng ????? quy???n ????? xem th??ng tin s???n ph???m n??y");

        return ResponseEntity.ok(productConverter.toProductResponse(product));
    }

    @Override
    public ResponseEntity<Object> listProductForRetailer(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Page<Product> productPage = productRepository.findByDeleteFlgFalseAndCreatedBy(authentication.getName(), pageable);
        Page<ShortProductResponse> page = productPage.map(product -> productConverter.toShortProductResponse(product));
        return ResponseEntity.ok(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> pageProductMostViewMonthForRetailer(String keyword, Pageable pageable, Integer month, Integer year) {
        // l???y th??ng username c???a retailer ??ang ????ng nh???p
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Query query;
        if (keyword != null && year != null && month != null) {
            // tr??? v??? t???t c???
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.createdBy = ?4 and s.product.enable = true and s.product.approve = true and lower(s.product.title) like concat('%',lower(?2) ,'%') and YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?3 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword)
                    .setParameter(3, month)
                    .setParameter(4, username);

        } else if (keyword != null && year == null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.createdBy = ?2 and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?1) ,'%') " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, keyword)
                    .setParameter(2, username);

        } else if (keyword != null && year != null && month == null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false  and s.product.createdBy = ?3 and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?2) ,'%') and YEAR(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, keyword)
                    .setParameter(3, username);

        } else if (keyword == null && year != null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false and s.product.createdBy = ?3 and s.product.enable = true and s.product.approve = true and   YEAR(s.statisticDay) = ?1 AND month(s.statisticDay) =  ?2 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, month)
                    .setParameter(3, username);
        } else if (keyword == null && year == null && month == null) {
            System.out.println("username: "+username);
            // tr??? v??? t???t c???
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product ) " +
                            "FROM ViewCountStatistic s where s.product.deleteFlg = false  and s.product.createdBy = ?1 and s.product.enable = true and s.product.approve = true " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, username);

        } else if (keyword == null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where  s.product.deleteFlg = false  and s.product.createdBy = ?2 and s.product.enable = true and s.product.approve = true and  month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month)
                    .setParameter(2, username);
        } else if (keyword != null && year == null && month != null) {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product) " +
                            "FROM ViewCountStatistic s " +
                            "WHERE s.product.deleteFlg = false  and s.product.createdBy = ?3 and s.product.enable = true and s.product.approve = true and  lower(s.product.title) like concat('%',lower(?2) ,'%') and month(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, month)
                    .setParameter(2, keyword)
                    .setParameter(3, username);
        } else {
            query = entityManager
                    .createQuery("SELECT  new dtu.thebestprice.payload.response.query.ViewCountModel(sum(s.viewCount) as viewcount, s.product)  " +
                            "FROM ViewCountStatistic s where  s.product.deleteFlg = false  and s.product.createdBy = ?2 and s.product.enable = true and s.product.approve = true and  year(s.statisticDay) = ?1 " +
                            "GROUP BY s.product " +
                            "ORDER BY viewcount desc ")
                    .setParameter(1, year)
                    .setParameter(2, username);
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
            throw new NumberFormatException("M?? nh?? b??n l??? kh??ng h???p l???");
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
            throw new Exception("M?? danh m???c kh??ng ????ng ?????nh d???ng");
        }

        if (category != null) {
            longSet.add(category.getId());
            if (category.getCategory() == null) {
                longSet.addAll(categoryRepository.findAllCatIdOfParent(category.getId()));
            }
        } else throw new Exception("Kh??ng t???n t???i id danh m???c");
        return longSet;
    }
}
