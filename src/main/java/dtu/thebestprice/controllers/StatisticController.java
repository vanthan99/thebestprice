package dtu.thebestprice.controllers;

import dtu.thebestprice.converters.DateConverter;
import dtu.thebestprice.payload.request.StatisticBetweenDayRequest;
import dtu.thebestprice.payload.request.StatisticRequest;
import dtu.thebestprice.services.SearchStatisticService;
import dtu.thebestprice.services.ViewCountStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/statistic")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Api
public class StatisticController {
    @Autowired
    ViewCountStatisticService viewCountStatisticService;

    @Autowired
    DateConverter dateConverter;

    @Autowired
    SearchStatisticService searchStatisticService;

    @ApiOperation(value = "Thống kê số lượt xem sản phẩm theo ngày truyền vào")
    @GetMapping("/viewCount")
    public ResponseEntity<Object> statisticViewCountByDate(
            @RequestParam("startDay") String strStartDay,
            @RequestParam("endDay") String strEndDay
    ) {
        LocalDate startDay = dateConverter.toStartDay(strStartDay);
        LocalDate endDay = dateConverter.toEndDay(strEndDay);

        if (endDay.isBefore(startDay))
            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");

        return viewCountStatisticService.statisticBetweenDay(startDay, endDay);
    }

    @ApiOperation(value = "Thống kê những từ khóa đã được tìm kiếm")
    @GetMapping("/statisticSearchByDateAndKeyword")
    public ResponseEntity<Object> statisticSearchByDateAndKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("month") String strMonth,
            @RequestParam("year") String strYear
    ) {
        int month;
        int year;

        // validate

        try {
            month = Integer.parseInt(strMonth);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Tháng phải là số nguyên");
        }

        try {
            year = Integer.parseInt(strYear);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("năm phải là số nguyên");
        }

        if (month < 1 || month > 12)
            throw new RuntimeException("Tháng từ 1 đến 12");
        if (year <= 2020 || year > LocalDate.now().getYear())
            throw new RuntimeException("Năm không được bé hơn năm 2021 và không được lớn hơn năm hiện tại");

        if (YearMonth.of(year,month).isAfter(YearMonth.now()))
            throw new RuntimeException("Tháng/năm truyền vào không được vượt quá hiện tại là "+YearMonth.now().toString());


            return searchStatisticService.byDateAndKeyword(keyword,year,month);
    }
}
