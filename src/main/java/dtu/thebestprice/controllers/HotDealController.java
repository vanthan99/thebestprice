package dtu.thebestprice.controllers;

import dtu.thebestprice.services.HotDealService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hotDeal")
public class HotDealController {
    @Autowired
    HotDealService hotDealService;

    @ApiOperation(value = "Top 10 sản phẩm hotdeal")
    @GetMapping("/top10Product")
    public ResponseEntity<Object> top10Product(){
        return hotDealService.top10Product();
    }

    @ApiOperation(value = "Top 10 laptop hotdeal")
    @GetMapping("/top10Laptop")
    public ResponseEntity<Object> top10Laptop(){
        return hotDealService.top10Laptop();
    }

    @ApiOperation(value = "Top 10 điện thoại hotdeal")
    @GetMapping("/top10Smartphone")
    public ResponseEntity<Object> top10Smartphone(){
        return hotDealService.top10Smartphone();
    }
}
