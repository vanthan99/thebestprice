package dtu.thebestprice.payload.response.price;

import lombok.Data;

@Data
public class PriceDetailResponse {
    private Long productRetailerId;
    private String productName;
    private String retailerName;
    private Long price;
    private String url;
    private boolean enable;
}
