package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticViewCountResponse {
    private Long viewCount;
    private Long productId;
    private String productTitle;
    private List<String> productImages;
}
