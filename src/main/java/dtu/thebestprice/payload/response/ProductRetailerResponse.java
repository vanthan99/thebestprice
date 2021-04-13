package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRetailerResponse {
    private RetailerResponse retailerResponse;
    private String url;
    private Long price;
}
