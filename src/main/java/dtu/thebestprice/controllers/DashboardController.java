package dtu.thebestprice.controllers;

import dtu.thebestprice.exports.StatisticExcelExporter;
import dtu.thebestprice.services.DashBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Api
@RequestMapping("/api/v1/dashboard")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class DashboardController {

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("/overView")
    @ApiOperation(value = "Gồm các thông số tổng quan: lượt truy cập, lượt tìm kiếm,số người dùng, chủ cửa hàng, tổng sản phẩm")
    public ResponseEntity<Object> overView() {
        return dashBoardService.overView();
    }

    @GetMapping("/statistic")
    @ApiOperation(value = "Thống kê số lượt truy cập, số lượt tìm kiếm và phân loại user")
    public ResponseEntity<Object> statistic(
            @RequestParam(value = "type", required = false) String type
    ) {
        if (type == null) {
            throw new RuntimeException("Không được để trống loại thống kê");
        }
        return dashBoardService.dashBoard(type);
    }

//    @GetMapping("/statisticAccess")
//    @ApiOperation(value = "Thống kê số lượt truy cập theo từng tháng")
//    public ResponseEntity<Object> statisticAccess() {
//        return dashBoardService.statisticAccess();
//    }
//
//    @GetMapping("/statisticAccessByQuarter")
//    @ApiOperation(value = "Thống kê số lượt truy cập theo từng quý")
//    public ResponseEntity<Object> statisticAccessByQuarter() {
//        return dashBoardService.statisticAccessByQuarter();
//    }
//
//    @GetMapping("/statisticSearch")
//    @ApiOperation(value = "Thống kê số lượt tìm kiếm của tháng hiện tại")
//    public ResponseEntity<Object> statisticSearch() {
//        return dashBoardService.statisticSearch();
//    }
//
//    @GetMapping("/statisticRateUser")
//    @ApiOperation(value = "Thống kê tỷ lệ người dùng sử dụng hệ thống. tỷ lệ người dùng đã đăng nhập và chưa")
//    public ResponseEntity<Object> rateUser() {
//        return dashBoardService.rateUser();
//    }
//
    @GetMapping("/statisticKeyword")
    @ApiOperation(value = "Thống kê số lượt tìm kiếm từ trước đến nay")
    public ResponseEntity<Object> statisticKeyword(
            @PageableDefault(size = 15) Pageable pageable
    ) {
        return dashBoardService.statisticKeyword(pageable);
    }

    @GetMapping("/export")
    @ApiOperation(value = "Tải file thống kê excel")
    public ResponseEntity<Object> exportToExcel() throws IOException {
        return dashBoardService.exportToExcel();
    }
}
