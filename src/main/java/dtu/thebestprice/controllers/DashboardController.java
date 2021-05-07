package dtu.thebestprice.controllers;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@RequestMapping("/api/v1/dashboard")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class DashboardController {

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("/overView")
    @ApiOperation(value = "Gồm các thông số tổng quan: lượt truy cập, lượt tìm kiếm,số người dùng, chủ cửa hàng, tổng sản phẩm")
    public ResponseEntity<Object> overView() {
        return dashBoardService.overView();
    }

    @GetMapping("/statisticAccess")
    @ApiOperation(value = "Thống kê số lượt truy cập của tháng hiện tại")
    public ResponseEntity<Object> statisticAccess(

    ) {
        return dashBoardService.statisticAccess();
    }

    @GetMapping("/statisticSearch")
    @ApiOperation(value = "Thống kê số lượt tìm kiếm")
    public ResponseEntity<Object> statisticSearch(
            @PageableDefault(size = 15) Pageable pageable
    ) {
        return dashBoardService.statisticKeyword(pageable);
    }
}
