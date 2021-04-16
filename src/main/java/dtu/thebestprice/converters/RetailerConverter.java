package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.payload.response.RetailerResponse;
import org.springframework.stereotype.Component;

@Component
public class RetailerConverter {
    public RetailerResponse toRetailerResponse(Retailer retailer){
        return new RetailerResponse(
                retailer.getId(),
                retailer.getDescription(),
                retailer.getHomePage(),
                retailer.getLogoImage()
        );
    }
}
