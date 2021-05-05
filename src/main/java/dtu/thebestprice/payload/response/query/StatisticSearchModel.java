package dtu.thebestprice.payload.response.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticSearchModel {
    private String keyword;
    private Long numberOfSearch;
}