package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.ProductItem;
import dtu.thebestprice.payload.response.ProductRetailerResponse;
import dtu.thebestprice.payload.response.ShortProductResponse;
import dtu.thebestprice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductConverter {
    @Autowired
    CategoryConverter categoryConverter;

    @Autowired
    BrandConverter brandConverter;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    ProductRetailerConverter productRetailerConverter;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    PriceRepository priceRepository;

    // mapping product request to product entity
    public Product toEntity(ProductRequest productRequest) {
        Product product = new Product();

        product.setTitle(productRequest.getTitle());
        product.setLongDescription(productRequest.getLongDescription());
        product.setShortDescription(productRequest.getShortDescription());

        // set category
        product.setCategory(categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("id danh mục không tồn tại!")));

        // set brand
        product.setBrand(brandRepository.findById(productRequest.getBrandId()).orElseThrow(() -> new RuntimeException("id nhà sản xuất không tồn tại")));
        return product;
    }

    public Product toEntity(ProductRequest productRequest, Product product) {

        /*
         * Trường nào có giá trị thì mới cập nhật
         * */

        if (productRequest.getTitle() != null && !productRequest.getTitle().trim().equalsIgnoreCase(""))
            product.setTitle(productRequest.getTitle());

        if (productRequest.getLongDescription() != null && !productRequest.getLongDescription().trim().equalsIgnoreCase(""))
            product.setLongDescription(productRequest.getLongDescription());

        if (productRequest.getShortDescription() != null && !productRequest.getShortDescription().trim().equalsIgnoreCase(""))
            product.setShortDescription(productRequest.getShortDescription());

        // set category
        if (productRequest.getCategoryId() != null)
            product.setCategory(categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("id danh mục không tồn tại!")));

        // set brand
        if (productRequest.getBrandId() != null)
            product.setBrand(brandRepository.findById(productRequest.getBrandId()).orElseThrow(() -> new RuntimeException("id nhà sản xuất không tồn tại")));

        return product;
    }


    public LongProductResponse toLongProductResponse(Product product) {
        LongProductResponse longProductResponse = new LongProductResponse();

        longProductResponse.setId(product.getId());
        longProductResponse.setTitle(product.getTitle());
        longProductResponse.setShortDescription(product.getShortDescription());
        longProductResponse.setLongDescription(product.getLongDescription());

        // set category
        longProductResponse.setCategory(categoryConverter.toShortCategoryResponse(product.getCategory()));

        // set brand
        longProductResponse.setBrand(brandConverter.toBrandResponse(product.getBrand()));

        // set list image
        List<String> images = new ArrayList<>();
        imageRepository.findByProductAndDeleteFlgFalse(product).forEach(image -> {
            images.add(image.getUrl());
        });
        longProductResponse.setImages(images);

        // set Rating
        longProductResponse.setRate((double) Math.round(ratingRepository.getRateByProduct(product.getId()) * 10) / 10);


        // set product retailer response
        List<ProductRetailer> productRetailers = productRetailerRepository.findByProductAndDeleteFlgFalse(product);
        List<ProductRetailerResponse> productRetailerResponseList =
                productRetailerConverter.toProductRetailerResponseList(productRetailers)
                        .stream()
                        .filter(productRetailerResponse -> productRetailerResponse.getPrice() != null)
                        .filter(productRetailerResponse -> productRetailerResponse.getPrice() != 0)
                        .collect(Collectors.toList());
        ;

        longProductResponse.setPrices(productRetailerResponseList);

        // set lowest and highest price
        LongSummaryStatistics statistics = this.summaryStatisticsPrice(productRetailerResponseList);
        longProductResponse.setLowestPrice(statistics.getCount() != 0 ? statistics.getMin() : 0);
        longProductResponse.setHighestPrice(statistics.getCount() != 0 ? statistics.getMax() : 0);


        return longProductResponse;
    }

    public ShortProductResponse toShortProductResponse(Product product) {
        ShortProductResponse response = new ShortProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setViewCount(product.getViewCount());
        response.setShortDescription(product.getShortDescription());

        // set Rating
        response.setRate((double) Math.round(ratingRepository.getRateByProduct(product.getId()) * 10) / 10);

        // set list image
        response.setImage(imageRepository.findFirstByProduct(product).getUrl());


        // set lowest and highest price
        List<ProductRetailer> productRetailers = productRetailerRepository.findByProductAndDeleteFlgFalse(product);
        List<Long> prices = new ArrayList<>();
        productRetailers
                .forEach(productRetailer -> {
                    prices.add(priceRepository.findByPriceLatestByProductRetailer(productRetailer));
                });

        LongSummaryStatistics statistics = this.summaryStatistics(prices);
        response.setHighestPrice(statistics.getMax());
        response.setLowestPrice(statistics.getMin());

        return response;
    }

    public ProductItem toProductItem(Product product) {
        ProductItem productItem = new ProductItem();
        productItem.setTitle(product.getTitle());
        productItem.setId(product.getId());

        /**/
        List<ProductRetailer> productRetailers = productRetailerRepository.findByProductAndDeleteFlgFalse(product);
        List<ProductRetailerResponse> productRetailerResponseList = productRetailerConverter.toProductRetailerResponseList(productRetailers);
        LongSummaryStatistics statistics = this.summaryStatisticsPrice(productRetailerResponseList);

        productItem.setPrice(statistics.getCount() != 0 ? statistics.getMin() : 0);

        productItem.setTotalStore((long) productRetailers.size());
        return productItem;
    }

    /*
     * Thống kê
     *
     * Trong Product Retailer Response có 1 thuộc tính price
     * trả về thống kê từ danh sách Product Retailer Response truyền vào.
     * */
    private LongSummaryStatistics summaryStatisticsPrice(List<ProductRetailerResponse> productRetailerResponses) {
        List<Long> prices = new ArrayList<>();

        productRetailerResponses.forEach(item -> {
            if (item != null) {
                if (item.getPrice() != null) {
                    prices.add(item.getPrice());
                }
            }
        });
        return prices.stream().mapToLong(Long::new).summaryStatistics();
    }

    // thống kê từ 1 mảng Long truyền vào
    private LongSummaryStatistics summaryStatistics(List<Long> longList) {
        longList = longList
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return longList.stream().mapToLong(Long::new).summaryStatistics();
    }

}
