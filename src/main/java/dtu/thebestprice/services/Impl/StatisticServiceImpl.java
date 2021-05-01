package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.SearchStatisticConverter;
import dtu.thebestprice.entities.SearchStatisticDay;
import dtu.thebestprice.entities.SearchStatisticYear;
import dtu.thebestprice.entities.SearchStatisticYearMonth;
import dtu.thebestprice.payload.response.SearchStatisticResponse;
import dtu.thebestprice.repositories.SearchStatisticDayRepository;
import dtu.thebestprice.repositories.SearchStatisticYearMonthRepository;
import dtu.thebestprice.repositories.SearchStatisticYearRepository;
import dtu.thebestprice.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    SearchStatisticDayRepository searchStatisticDayRepository;

    @Autowired
    SearchStatisticYearMonthRepository searchStatisticYearMonthRepository;

    @Autowired
    SearchStatisticYearRepository searchStatisticYearRepository;

    @Autowired
    SearchStatisticConverter searchStatisticConverter;

    @Override
    public ResponseEntity<Object> statisticSearchBySingleDay(LocalDate date) {
        List<SearchStatisticDay> searchStatistics = searchStatisticDayRepository.findFirst10ByStatisticDayOrderByTotalOfSearchDesc(date);
        if (searchStatistics.size() > 0) {
            SearchStatisticResponse result = new SearchStatisticResponse();
            result.setTitle("Thống kê tìm kiếm ngày " + date.toString());
            result.setContent(searchStatisticConverter.toListSearchItemDay(searchStatistics));
            result.setTotalOfElements(searchStatistics.size());

            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body(new SearchStatisticResponse());
    }

    @Override
    public ResponseEntity<Object> statisticSearchByMonth(int month, int year) {
        List<SearchStatisticYearMonth> searchStatistics =
                searchStatisticYearMonthRepository.findFirst10ByStatisticMonthAndStatisticYearOrderByTotalOfSearchDesc(month, year);
        if (searchStatistics.size() > 0) {
            SearchStatisticResponse result = new SearchStatisticResponse();
            result.setTitle("Thống kê tìm kiếm tháng " + month + " - " + year);
            result.setContent(searchStatisticConverter.toListSearchItemMonth(searchStatistics));
            result.setTotalOfElements(searchStatistics.size());

            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body(new SearchStatisticResponse());
    }

    @Override
    public ResponseEntity<Object> statisticSearchByYear(int year) {
        List<SearchStatisticYear> searchStatistics = searchStatisticYearRepository.findFirst10ByStatisticYearOrderByTotalOfSearchDesc(year);
        if (searchStatistics.size() > 0) {
            SearchStatisticResponse result = new SearchStatisticResponse();
            result.setTitle("Thống kê tìm kiếm năm " + year);
            result.setContent(searchStatisticConverter.toListSearchItemYear(searchStatistics));
            result.setTotalOfElements(searchStatistics.size());

            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body(new SearchStatisticResponse());
    }
}
