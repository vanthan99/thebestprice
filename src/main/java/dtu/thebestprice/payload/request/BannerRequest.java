package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequest {
    @NotBlank(message = "Không để trống tiêu đề")
    @Size(min = 5, max = 50, message = "Tiêu đề từ 5 - 50 ký tự")
    private String title;

    @NotBlank(message = "Không để trống mô tả")
    @Size(min = 10, max = 100, message = "mô tả từ 5 - 50 ký tự")
    private String description;

    @URL(message = "url hình ảnh khôgn đúng định dạng")
    @Size(min = 5, max = 255, message = "url hình ảnh từ 5 - 255 ký tự")
    private String imageUrl;

    @URL(message = "url gian hàng khôgn đúng định dạng")
    @Size(min = 5, max = 255, message = "url gian hàng từ 5 - 255 ký tự")
    private String redirectUrl;
}
