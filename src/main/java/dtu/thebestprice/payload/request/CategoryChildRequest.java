package dtu.thebestprice.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Danh mục con model")
public class CategoryChildRequest {
    @NotBlank(message = "Không được để trống tên danh mục")
    @Size(min = 5,max = 50,message = "Tiêu đề từ 5-50 ký tự")
    @ApiModelProperty(notes = "Tiêu đề danh mục")
    private String title;

    @NotBlank(message = "Không được để trống mô tả danh mục")
    @ApiModelProperty(notes = "Mô tả danh mục")
    private String description;

    @NotNull(message = "Không được để trống id của danh mục cha")
    @ApiModelProperty(notes = "Id của danh mục cha")
    private Long parentId;
}
