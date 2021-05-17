package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Brand;
import dtu.thebestprice.payload.request.brand.BrandRequest;
import dtu.thebestprice.payload.response.BrandResponse;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {
    public BrandResponse toBrandResponse(Brand brand) {
        if (brand == null)
            return null;
        return new BrandResponse(brand.getId(), brand.getName());
    }

    public Brand toEntity(BrandRequest brandRequest, Brand brand) {
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());

        return brand;
    }
}
