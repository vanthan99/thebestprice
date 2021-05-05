package dtu.thebestprice.payload.response.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
* class item chứa ngày thống kê vào tổng số lượt tìm kiếm của ngày đó
* */
public class StatisticCountSearchModel {
    private LocalDate dateStatistic;
    private Long total;
}
