package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LongProductResponse {
    private Long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private ShortCategoryResponse shortCategoryResponse;
    private BrandResponse brandResponse;
    private List<String> images;
    private Long lowestPrice;
    private Long highestPrice;
    private List<ProductRetailerResponse> listStores;

}
