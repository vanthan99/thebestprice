package dtu.thebestprice.converters;

import dtu.thebestprice.entities.SearchStatisticDay;
import dtu.thebestprice.entities.SearchStatisticYear;
import dtu.thebestprice.entities.SearchStatisticYearMonth;
import dtu.thebestprice.payload.response.SearchStatisticItemResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchStatisticConverter {
    public SearchStatisticItemResponse toSearchItemDay(SearchStatisticDay searchStatisticDay) {
        return new SearchStatisticItemResponse(
                searchStatisticDay.getSearch().getKeyword(),
                searchStatisticDay.getTotalOfSearch()
        );
    }

    public SearchStatisticItemResponse toSearchItemMonth(SearchStatisticYearMonth searchStatisticMonth) {
        return new SearchStatisticItemResponse(
                searchStatisticMonth.getSearch().getKeyword(),
                searchStatisticMonth.getTotalOfSearch()
        );
    }

    public SearchStatisticItemResponse toSearchItemYear(SearchStatisticYear searchStatisticYear) {
        return new SearchStatisticItemResponse(
                searchStatisticYear.getSearch().getKeyword(),
                searchStatisticYear.getTotalOfSearch()
        );
    }

    public List<SearchStatisticItemResponse> toListSearchItemDay(List<SearchStatisticDay> searchStatistics) {
        List<SearchStatisticItemResponse> result = new ArrayList<>();
        searchStatistics.forEach(searchStatistic -> result.add(this.toSearchItemDay(searchStatistic)));
        return result;
    }

    public List<SearchStatisticItemResponse> toListSearchItemMonth(List<SearchStatisticYearMonth> searchStatistics) {
        List<SearchStatisticItemResponse> result = new ArrayList<>();
        searchStatistics.forEach(searchStatistic -> result.add(this.toSearchItemMonth(searchStatistic)));
        return result;
    }

    public List<SearchStatisticItemResponse> toListSearchItemYear(List<SearchStatisticYear> searchStatistics) {
        List<SearchStatisticItemResponse> result = new ArrayList<>();
        searchStatistics.forEach(searchStatistic -> result.add(this.toSearchItemYear(searchStatistic)));
        return result;
    }
}
