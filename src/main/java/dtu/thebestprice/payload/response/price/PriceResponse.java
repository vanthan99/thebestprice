package dtu.thebestprice.payload.response.price;

import dtu.thebestprice.payload.response.RetailerResponse;
import lombok.Data;

@Data
public class PriceResponse {
    private RetailerResponse retailer;
    private String urlProduct;
    private Long latestPrice;
    private Long productRetailerId;
    private boolean productRetailerEnable;
    private boolean productRetailerApprove;
}
