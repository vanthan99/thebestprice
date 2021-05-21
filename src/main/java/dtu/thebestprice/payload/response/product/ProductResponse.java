package dtu.thebestprice.payload.response.product;

import dtu.thebestprice.payload.response.BrandResponse;
import dtu.thebestprice.payload.response.ProductRetailerResponse;
import dtu.thebestprice.payload.response.ShortCategoryResponse;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private ShortCategoryResponse category;
    private BrandResponse brand;
    private List<String> images;
}
