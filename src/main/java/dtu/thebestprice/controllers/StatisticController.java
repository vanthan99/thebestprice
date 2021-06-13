package dtu.thebestprice.controllers;

import dtu.thebestprice.converters.DateConverter;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.services.SearchStatisticService;
import dtu.thebestprice.services.StatisticAccessService;
import dtu.thebestprice.services.ViewCountStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/statistic")
@Api
public class StatisticController {
    @Autowired
    ViewCountStatisticService viewCountStatisticService;

    @Autowired
    DateConverter dateConverter;

    @Autowired
    SearchStatisticService searchStatisticService;

    @Autowired
    StatisticAccessService statisticAccessService;

    @Autowired
    ProductService productService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

        return viewCountStatisticService.statisticBetweenDay(
                Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thống kê những từ khóa đã được tìm kiếm")
    @GetMapping("/statisticSearchByDateAndKeyword")
    public ResponseEntity<Object> statisticSearchByDateAndKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "month", required = false) String strMonth,
            @RequestParam(value = "year", required = false) String strYear,
            Pageable pageable
    ) {
        Integer month = null;
        Integer year = null;

        // validate
        if (strMonth != null)
            try {
                month = Integer.parseInt(strMonth);
                if (month < 1 || month > 12)
                    throw new RuntimeException("Tháng từ 1 đến 12");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Tháng phải là số nguyên");
            }
        if (strYear != null)
            try {
                year = Integer.parseInt(strYear);
                if (year <= 2020 || year > LocalDate.now().getYear())
                    throw new RuntimeException("Năm không được bé hơn năm 2021 và không được lớn hơn năm hiện tại");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("năm phải là số nguyên");
            }

        if (strYear != null && strMonth != null) {
            if (YearMonth.of(year, month).isAfter(YearMonth.now()))
                throw new RuntimeException("Tháng/năm truyền vào không được vượt quá hiện tại là " + YearMonth.now().toString());
        }

        return searchStatisticService.byDateAndKeyword(keyword, year, month, pageable);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thống kê những những sản phẩm được xem nhiều nhất")
    @GetMapping("/statisticProductByDateAndKeyword")
    public ResponseEntity<Object> statisticViewCountByDateAndKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "month", required = false) String strMonth,
            @RequestParam(value = "year", required = false) String strYear,
            Pageable pageable
    ) {
        Integer month = null;
        Integer year = null;

        // validate
        if (strMonth != null)
            try {
                month = Integer.parseInt(strMonth);
                if (month < 1 || month > 12)
                    throw new RuntimeException("Tháng từ 1 đến 12");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Tháng phải là số nguyên");
            }
        if (strYear != null)
            try {
                year = Integer.parseInt(strYear);
                if (year <= 2020 || year > LocalDate.now().getYear())
                    throw new RuntimeException("Năm không được bé hơn năm 2021 và không được lớn hơn năm hiện tại");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("năm phải là số nguyên");
            }

        if (strYear != null && strMonth != null) {
            if (YearMonth.of(year, month).isAfter(YearMonth.now()))
                throw new RuntimeException("Tháng/năm truyền vào không được vượt quá hiện tại là " + YearMonth.now().toString());
        }

        return productService.pageProductMostViewMonth(keyword, pageable, month, year);
    }

    // thống kê những sản phẩm của retailer được xem nhiều nhất
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    @ApiOperation(value = "Thống kê những những sản phẩm của retailer được xem nhiều nhất")
    @GetMapping("/statisticProductByDateAndKeywordForRetailer")
    public ResponseEntity<Object> statisticViewCountByDateAndKeywordForRetailer(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "month", required = false) String strMonth,
            @RequestParam(value = "year", required = false) String strYear,
            Pageable pageable
    ) {
        Integer month = null;
        Integer year = null;

        // validate
        if (strMonth != null)
            try {
                month = Integer.parseInt(strMonth);
                if (month < 1 || month > 12)
                    throw new RuntimeException("Tháng từ 1 đến 12");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Tháng phải là số nguyên");
            }
        if (strYear != null)
            try {
                year = Integer.parseInt(strYear);
                if (year <= 2020 || year > LocalDate.now().getYear())
                    throw new RuntimeException("Năm không được bé hơn năm 2021 và không được lớn hơn năm hiện tại");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("năm phải là số nguyên");
            }

        if (strYear != null && strMonth != null) {
            if (YearMonth.of(year, month).isAfter(YearMonth.now()))
                throw new RuntimeException("Tháng/năm truyền vào không được vượt quá hiện tại là " + YearMonth.now().toString());
        }

        return productService.pageProductMostViewMonthForRetailer(keyword, pageable, month, year);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thống kê lượt truy cập theo ngày")
    @GetMapping("/access")
    public ResponseEntity<Object> access(
            @RequestParam("startDay") String strStartDay,
            @RequestParam("endDay") String strEndDay
    ){
        LocalDate startDay = dateConverter.toStartDay(strStartDay);
        LocalDate endDay = dateConverter.toEndDay(strEndDay);

        if (endDay.isBefore(startDay))
            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");

        return statisticAccessService.staticBetweenDay(
                Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thống kê lượt tìm kiếm theo ngày")
    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam("startDay") String strStartDay,
            @RequestParam("endDay") String strEndDay
    ){
        LocalDate startDay = dateConverter.toStartDay(strStartDay);
        LocalDate endDay = dateConverter.toEndDay(strEndDay);

        if (endDay.isBefore(startDay))
            throw new RuntimeException("Ngày kết thúc phải bằng hoăc lớn hon ngày bắt đầu");

        return searchStatisticService.statisticBetweenDay(
                Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}
