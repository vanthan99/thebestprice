package dtu.thebestprice.controllers;

import dtu.thebestprice.services.StatisticService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @PostMapping("/searchByDay")
    @ApiOperation(value = "Thống kê theo ngày truyền vào")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> statisticBySearchByDay(
            @RequestParam("day") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate statisticDay
    ) {

        return statisticService.statisticSearchBySingleDay(statisticDay);

    }

    @PostMapping("/searchByMonth")
    @ApiOperation(value = "Thống kê theo tháng truyền vào")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> statisticBySearchByMonth(
            @RequestParam("month") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate statisticMonth
    ) {
        return statisticService.statisticSearchByMonth(statisticMonth.getMonthValue(),statisticMonth.getYear());
    }

    @PostMapping("/searchByYear")
    @ApiOperation(value = "Thống kê theo tháng truyền vào")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> statisticBySearchByYear(
            @RequestParam("year") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate statisticYear
    ) {
        return statisticService.statisticSearchByYear(statisticYear.getYear());
    }
}
