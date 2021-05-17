package dtu.thebestprice.converters;

import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.response.price.PriceResponse;
import dtu.thebestprice.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceConverter {
    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    PriceRepository priceRepository;

    public PriceResponse toPriceResponse(ProductRetailer productRetailer) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailer(retailerConverter.toRetailerResponse(productRetailer.getRetailer()));
        priceResponse.setUrlProduct(productRetailer.getUrl());
        priceResponse.setLatestPrice(priceRepository.findByPriceLatestByProductRetailer(productRetailer));
        priceResponse.setProductRetailerId(productRetailer.getId());
        return priceResponse;
    }
}
