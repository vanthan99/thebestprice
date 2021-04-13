package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Brand;
import dtu.thebestprice.payload.response.BrandResponse;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {
    public BrandResponse toBrandResponse(Brand brand){
        if (brand == null)
            return null;
        return new BrandResponse(brand.getId(), brand.getName());
    }
}
