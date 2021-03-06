package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryParentRequest {
    @NotBlank(message = "Không được để trống tên danh mục")
    @Size(min = 5,max = 50,message = "Tiêu đề từ 5-50 ký tự")
    private String title;

    @NotBlank(message = "Không được để trống mô tả của danh mục")
    @Size(min = 5,message = "Mô tả ít nhất 5 ký tự")
    private String description;
}
