package dtu.thebestprice.controllers;

import dtu.thebestprice.converters.DateConverter;
import dtu.thebestprice.payload.request.QuarterRequest;
import dtu.thebestprice.payload.request.StatisticBetweenDayRequest;
import dtu.thebestprice.payload.request.StatisticBetweenYearRequest;
import dtu.thebestprice.services.ViewCountStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/viewCountStatistic")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Api
public class ViewCountStatisticController {
//    @Autowired
//    ViewCountStatisticService viewCountStatisticService;
//
//    @Autowired
//    DateConverter dateConverter;
//
//    @PostMapping("/betweenDay")
//    @ApiOperation(value = "Những sản phẩm được xem nhiều nhất từ ngày bắt đầu tới ngày kết thúc")
//    public ResponseEntity<Object> statisticBetweenDay(
//            @RequestBody @Valid StatisticBetweenDayRequest request,
//            Pageable pageable
//    ) {
//        LocalDate startDay = dateConverter.toStartDay(request.getStartDay());
//        LocalDate endDay = dateConverter.toEndDay(request.getEndDay());
//
//        if (endDay.isBefore(startDay))
//            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");
//
//        return viewCountStatisticService.statisticBetweenDay(startDay, endDay, pageable);
//    }
//
//    @PostMapping("/quarter")
//    @ApiOperation(value = "Những sản phẩm được xem nhiều nhất theo quý")
//    public ResponseEntity<Object> statisticByQuarter(
//            @RequestBody @Valid QuarterRequest request,
//            Pageable pageable
//    ) {
//        int quarter = dateConverter.toQuarter(request.getQuarter());
//        int year = dateConverter.toYear(request.getYear());
//
//
//
//        return viewCountStatisticService.statisticByQuarter(year, quarter, pageable);
//    }
//
//    @PostMapping("/betweenYear")
//    @ApiOperation(value = "Những sản phẩm được xem nhiều nhất từ năm bắt đầu tới năm kết thúc")
//    public ResponseEntity<Object> statisticBetweenYear(
//            @RequestBody @Valid StatisticBetweenYearRequest request,
//            Pageable pageable
//    ) {
//        int startYear = dateConverter.toStartYear(request.getStartYear());
//        int endYear = dateConverter.toEndYear(request.getEndYear());
//
//        if (endYear < startYear)
//            throw new RuntimeException("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu");
//
//        return viewCountStatisticService.statisticBetweenYear(startYear, endYear, pageable);
//    }
//
//    @GetMapping("/top20ProductViewedMostDay")
//    @ApiOperation(value = "Top 20 sản phẩm được xem nhiều nhất ngày hôm nay")
//    public ResponseEntity<Object> top20ProductViewedMostByDateDay(){
//        LocalDate nowDay = LocalDate.now();
//        return viewCountStatisticService.top20ProductViewedMostByDateDay(nowDay);
//    }
}
