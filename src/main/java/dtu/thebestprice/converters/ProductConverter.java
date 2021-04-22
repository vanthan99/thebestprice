package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.ProductItem;
import dtu.thebestprice.payload.response.ProductRetailerResponse;
import dtu.thebestprice.repositories.ImageRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

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
        longProductResponse.setRate(ratingRepository.getRateByProduct(product.getId()));


        // set product retailer response
        List<ProductRetailer> productRetailers = productRetailerRepository.findByProductAndDeleteFlgFalse(product);
        List<ProductRetailerResponse> productRetailerResponseList = productRetailerConverter.toProductRetailerResponseList(productRetailers);
        longProductResponse.setPrices(productRetailerResponseList);

        // set lowest and highest price
        LongSummaryStatistics statistics = this.summaryStatisticsPrice(productRetailerResponseList);
        longProductResponse.setLowestPrice(statistics.getCount() != 0 ? statistics.getMin() : 0);
        longProductResponse.setHighestPrice(statistics.getCount() != 0 ? statistics.getMax() : 0);


        return longProductResponse;
    }

    public ProductItem toProductItem(Product product){
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

}
