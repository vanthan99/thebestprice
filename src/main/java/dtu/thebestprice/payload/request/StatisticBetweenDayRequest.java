package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StatisticBetweenDayRequest {
    @NotNull(message = "Không được để trống ngày bắt đầu")
    private String startDay;

    @NotNull(message = "Không được để trống ngày kết thúc")
    private String endDay;
}
