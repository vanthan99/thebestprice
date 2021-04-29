package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryParentRequest {
    private Long id;

    @NotBlank(message = "Không được để trống tên danh mục")
    @Size(min = 5,max = 50,message = "Tiêu đề từ 5-50 ký tự")
    private String title;

    @NotBlank(message = "Không được để trống mô tả của danh mục")
    private String description;
}
