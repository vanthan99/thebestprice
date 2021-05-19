package dtu.thebestprice.payload.request.price;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PriceRetailerRequest {
    @NotBlank(message = "Không được để trống retailer id")
    private String retailerId;

    @URL(message = "đường dẫn không đúng định dạng")
    @Size(min = 50,max = 255,message = "Độ dài url từ 50 - 255 ký tự")
    private String url;

    @NotBlank(message = "Không được để trống giá")
    private String price;
}
