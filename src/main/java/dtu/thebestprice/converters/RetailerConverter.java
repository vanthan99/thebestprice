package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.payload.request.RetailerForUserRequest;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.RetailerResponse;
import org.springframework.stereotype.Component;

@Component
public class RetailerConverter {
    public RetailerResponse toRetailerResponse(Retailer retailer){
        return new RetailerResponse(
                retailer.getId(),
                retailer.getName(),
                retailer.getDescription(),
                retailer.getHomePage(),
                retailer.getLogoImage()
        );
    }

    public Retailer toEntity(RetailerRequest retailerRequest){
        Retailer retailer = new Retailer();
        retailer.setName(retailerRequest.getName().trim());
        retailer.setDescription(retailerRequest.getDescription().trim());
        retailer.setLogoImage(retailerRequest.getLogo().trim());
        retailer.setHomePage(retailerRequest.getHomePage().trim());
        return retailer;
    }

    public Retailer toEntity(RetailerForUserRequest retailerRequest){
        Retailer retailer = new Retailer();
        retailer.setName(retailerRequest.getName().trim());
        retailer.setDescription(retailerRequest.getDescription().trim());
        retailer.setLogoImage(retailerRequest.getLogo().trim());
        retailer.setHomePage(retailerRequest.getHomePage().trim());
        return retailer;
    }

    public Retailer toEntity(RetailerRequest retailerRequest, Retailer retailer){
        retailer.setName(retailerRequest.getName().trim());
        retailer.setDescription(retailerRequest.getDescription().trim());
        retailer.setLogoImage(retailerRequest.getLogo().trim());
        retailer.setHomePage(retailerRequest.getHomePage().trim());
        return retailer;
    }
}
