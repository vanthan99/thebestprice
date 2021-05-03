package dtu.thebestprice.payload.request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
public class ProductRequest {
    @NotBlank(message = "Không được để trống tiêu đề")
    @Size(min = 10,max = 255,message = "Tiêu đề nằm trong khoảng 10 - 255 ký tự")
    private String title;

    @NotBlank(message = "Không được để trống mô tả chi tiết")
    @Size(min = 10,message = "Mô tả chi tiết phải nhiều hơn 10 ký tự")
    private String longDescription;

    @NotBlank(message = "Không được để trống mô tả ngắn")
    @Size(min = 10,message = "Mô tả ngắn phải nhiều hơn 10 ký tự")
    private String shortDescription;

    @NotNull(message = "Không được để trống id của danh mục")
    private Long categoryId;

    @NotNull(message = "Không được để trống id của nhà sản xuất")
    private Long brandId;

    // list hình ảnh
    private List<
            @URL(message = "URL hình ảnh không đúng định dạng")
            @Size(max = 255,message = "Hình ảnh không vượt quá 255 ký tự")
                    String> images;
}
