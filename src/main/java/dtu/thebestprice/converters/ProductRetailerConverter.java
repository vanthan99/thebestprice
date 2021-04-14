package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.payload.response.ProductRetailerResponse;
import dtu.thebestprice.repositories.PriceRepository;
import dtu.thebestprice.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductRetailerConverter {
    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    PriceRepository priceRepository;


    public ProductRetailerResponse toProductRetailerResponse(ProductRetailer productRetailer){
        ProductRetailerResponse productRetailerResponse = new ProductRetailerResponse();

        Retailer retailer = retailerRepository.findById(productRetailer.getRetailer().getId()).orElse(null);

        // kiểm tra nếu retailer null thì set product retailer là null.
        if (retailer == null)
            productRetailerResponse.setStore(null);
        else productRetailerResponse.setStore(retailerConverter.toRetailerResponse(retailer));


        // set url product
        productRetailerResponse.setUrl(productRetailer.getUrl());

        // set price
        Price price = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
        productRetailerResponse.setPrice(price.getPrice());

        return productRetailerResponse;
    }

    public List<ProductRetailerResponse> toProductRetailerResponseList(List<ProductRetailer> productRetailerResponses){
        List<ProductRetailerResponse> list = new ArrayList<>();
        productRetailerResponses.forEach(productRetailerResponse -> list.add(this.toProductRetailerResponse(productRetailerResponse)));
        return list;
    }
}
