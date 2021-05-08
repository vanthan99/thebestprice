package dtu.thebestprice.controllers;

import dtu.thebestprice.converters.DateConverter;
import dtu.thebestprice.payload.request.QuarterRequest;
import dtu.thebestprice.payload.request.StatisticBetweenDayRequest;
import dtu.thebestprice.payload.request.StatisticBetweenYearRequest;
import dtu.thebestprice.services.SearchStatisticService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/searchStatistic")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class SearchStatisticController {
//    @Autowired
//    SearchStatisticService statisticService;
//
//    @Autowired
//    DateConverter dateConverter;
//
//
//    @PostMapping("/betweenDay")
//    @ApiOperation(value = "Thống kê những từ được tìm kiếm theo từ ngày bắt đầu tới ngày kết thúc")
//    public ResponseEntity<Object> betweenDay(
//            @RequestBody @Valid StatisticBetweenDayRequest request,
//            @PageableDefault(size = 10) Pageable pageable
//    ) {
//        LocalDate startDay = dateConverter.toStartDay(request.getStartDay());
//        LocalDate endDay = dateConverter.toEndDay(request.getEndDay());
//
//        if (endDay.isBefore(startDay))
//            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");
//
//        return statisticService.statisticDateBetween(startDay, endDay, pageable);
//
//
//    }
//
//    @PostMapping("/quarter")
//    @ApiOperation(value = "Thống kê những từ được tìm kiếm theo quý")
//    public ResponseEntity<Object> quarter(
//            @RequestBody @Valid QuarterRequest request,
//            Pageable pageable
//    ) {
//        int quarter = dateConverter.toQuarter(request.getQuarter());
//        int year = dateConverter.toYear(request.getYear());
//        return statisticService.statisticByQuarter(quarter,year,pageable);
//    }
//
//    @PostMapping("/betweenYear")
//    @ApiOperation(value = "Thống kê những từ được tìm kiếm theo từ năm bắt đầu tới năm kết thúc")
//    public ResponseEntity<Object> betweenYear(
//            @RequestBody @Valid StatisticBetweenYearRequest request,
//            Pageable pageable
//    ) {
//        int startYear = dateConverter.toStartYear(request.getStartYear());
//        int endYear = dateConverter.toEndYear(request.getEndYear());
//
//        if (endYear < startYear)
//            throw new RuntimeException("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu");
//
//        return statisticService.statisticYearBetween(startYear, endYear, pageable);
//    }
//
//    @ApiOperation(value = "Thống kê số lần tìm kiếm từ ngày bắt đầu tới ngày kết thúc")
//    @PostMapping("/searchCount/betweenDay")
//    public ResponseEntity<Object> searchCountBetweenDay(
//            @RequestBody @Valid StatisticBetweenDayRequest request
//    ) {
//
//        LocalDate startDay = dateConverter.toStartDay(request.getStartDay());
//        LocalDate endDay = dateConverter.toEndDay(request.getEndDay());
//
//        if (endDay.isBefore(startDay))
//            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");
//
//        return statisticService.countSearchByBetweenDay(startDay, endDay);
//    }
//
//    @ApiOperation(value = "Thống kê số lần tìm kiếm theo quý")
//    @PostMapping("/searchCountByQuarter")
//    public ResponseEntity<Object> searchCountByQuarter(
//            @RequestBody @Valid QuarterRequest request
//    ) {
//        int quarter = dateConverter.toQuarter(request.getQuarter());
//        int year = dateConverter.toYear(request.getYear());
//
//        return statisticService.countSearchByQuarter(quarter, year);
//    }
//
//    @ApiOperation(value = "Thống kê số lần tìm kiếm từ năm bắt đầu tới năm kết thúc")
//    @PostMapping("/searchCount/betweenYear")
//    public ResponseEntity<Object> searchCountBetweenYear(
//            @RequestBody @Valid StatisticBetweenYearRequest request
//    ) {
//        int startYear = dateConverter.toStartYear(request.getStartYear());
//        int endYear = dateConverter.toEndYear(request.getEndYear());
//
//        if (endYear < startYear)
//            throw new RuntimeException("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu");
//
//        return statisticService.countSearchByBetweenYear(startYear, endYear);
//    }

}
