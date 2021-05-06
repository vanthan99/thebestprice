package dtu.thebestprice.payload.response.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticCountSearchByQuarterModel {
    private int year;
    private int quarter;
    private Long total;
}
