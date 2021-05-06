package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuarterRequest {
    @NotBlank(message = "Không được để trống năm")
    private String year;

    @NotBlank(message = "Không được để trống quý")
    private String quarter;
}
