package dtu.thebestprice.controllers;

import dtu.thebestprice.services.RetailerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/api/v1/retailer")
public class RetailerController {
    @Autowired
    RetailerService retailerService;

    @ApiOperation(value = "Danh sách nhà bán lẻ")
    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok(retailerService.findAll());
    }
}
