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
    private ShortCategoryResponse category;
    private BrandResponse brand;
    private List<String> images;
    private Double rate;
    private Long lowestPrice;
    private Long highestPrice;
    private List<ProductRetailerResponse> prices;
    private Long totalRate;
    private boolean approve;

    // kiểm tra xem người xem hiện tại đã rate hay chưa?
    private boolean rated;
}
