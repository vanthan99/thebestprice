package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StatisticBetweenYearRequest {
    @NotBlank(message = "Không được để trống năm bắt đầu")
    private String startYear;
    @NotBlank(message = "Không được để trống năm kết thúc")
    private String endYear;
}
