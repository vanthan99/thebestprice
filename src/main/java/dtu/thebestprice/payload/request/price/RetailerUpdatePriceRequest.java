package dtu.thebestprice.payload.request.price;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RetailerUpdatePriceRequest {
    @NotBlank(message = "Không được để trống product retailer id")
    private String productRetailerId;

    @NotBlank(message = "Không được để trống giá cập nhật")
    private String price;
}
