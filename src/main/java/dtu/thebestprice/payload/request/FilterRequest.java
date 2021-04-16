package dtu.thebestprice.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel(value = "Filter model")
public class FilterRequest {

    @ApiModelProperty(notes = "từ khóa cần lọc")
    private String keyword;

    @ApiModelProperty(notes = "mã danh mục cần lọc")
    private Set<String> catIds;
}
