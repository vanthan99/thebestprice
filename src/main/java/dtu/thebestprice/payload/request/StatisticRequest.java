package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StatisticRequest {
    private String keyword;

    @NotBlank(message = "Không được để trống tháng thống kê")
    private String month;

    @NotBlank(message = "Không được để trống năm thống kê")
    private String year;
}
