package dtu.thebestprice.payload.request.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    @NotBlank(message = "Không được để trống tên")
    private String name;

    @NotBlank(message = "Không được để trống mô tả")
    private String description;
}
