package dtu.thebestprice.payload.request.product;

import dtu.thebestprice.payload.request.ProductRequest;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ProductFullRequest {
    @Valid
    private ProductRequest product;

    @URL(message = "Đường dẫn sản phẩm tới nơi bán không đúng định dạng")
    @Size(min = 50,max = 255, message = "đường dẫn từ 50 - 255 ký tự")
    private String url;

    @NotBlank(message = "Không được để trống giá")
    private String price;

    @NotBlank(message = "Không được để trống id nhà bán lẽ")
    private String retailerId;
}
