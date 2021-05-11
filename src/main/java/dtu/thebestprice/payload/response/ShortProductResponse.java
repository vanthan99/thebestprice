package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortProductResponse {
    private Long id;
    private String title;
    private String image;
    private Double rate;
    private Long lowestPrice;
    private Long highestPrice;
    private Long viewCount;
}
