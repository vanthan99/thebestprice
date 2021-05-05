package dtu.thebestprice.payload.request;

import lombok.Data;

@Data
public class StatisticBetweenMonthRequest {
    private String startMonth;
    private String endMonth;
}
