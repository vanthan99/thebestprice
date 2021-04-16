package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.ProductRetailerResponse;
import dtu.thebestprice.repositories.ImageRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
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


    public LongProductResponse toLongProductResponse(Product product){
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


        // set product retailer response
        List<ProductRetailer> productRetailers = productRetailerRepository.findByProductAndDeleteFlgFalse(product);
        List<ProductRetailerResponse> productRetailerResponseList = productRetailerConverter.toProductRetailerResponseList(productRetailers);
        longProductResponse.setPrices(productRetailerResponseList);

        // set lowest and highest price
        LongSummaryStatistics statistics = this.summaryStatisticsPrice(productRetailerResponseList);
        longProductResponse.setLowestPrice(statistics.getMin());
        longProductResponse.setHighestPrice(statistics.getMax());


        return longProductResponse;
    }


    /*
    * Thống kê
    *
    * Trong Product Retailer Response có 1 thuộc tính price
    * trả về thống kê từ danh sách Product Retailer Response truyền vào.
    * */
    private LongSummaryStatistics summaryStatisticsPrice(List<ProductRetailerResponse> productRetailerResponses){
        List<Long> prices = new ArrayList<>();

        productRetailerResponses.forEach(item -> {
            if (item != null){
                prices.add(item.getPrice());
            }
        });
        return prices.stream().mapToLong(Long::new).summaryStatistics();
    }

}
