package dtu.thebestprice.converters;

import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.payload.response.price.PriceDetailResponse;
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
        priceResponse.setCreatedBy(productRetailer.getCreatedBy());
        priceResponse.setRetailer(retailerConverter.toRetailerResponse(productRetailer.getRetailer()));
        priceResponse.setUrlProduct(productRetailer.getUrl());
        priceResponse.setLatestPrice(priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer).getPrice());
        priceResponse.setProductRetailerId(productRetailer.getId());
        priceResponse.setApprove(productRetailer.isApprove());
        priceResponse.setEnable(productRetailer.isEnable());
        return priceResponse;
    }

    public PriceDetailResponse toPriceDetailResponse(ProductRetailer productRetailer) {
        PriceDetailResponse result = new PriceDetailResponse();

        result.setProductRetailerId(productRetailer.getId());
        result.setProductName(productRetailer.getProduct().getTitle());
        result.setPrice(priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer).getPrice());
        result.setUrl(productRetailer.getUrl());
        result.setEnable(productRetailer.isEnable());
        result.setRetailerName(productRetailer.getRetailer().getName());

        return result;
    }
}
