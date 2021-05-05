package dtu.thebestprice.payload.response.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
* Class chứa thông tin năm thống kê và số lượt tìm kiếm của năm đó
* */
public class StatisticCountSearchByYearModel {
    private int statisticYear;
    private Long total;
}
