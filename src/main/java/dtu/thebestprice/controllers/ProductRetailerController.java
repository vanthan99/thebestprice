package dtu.thebestprice.controllers;

import dtu.thebestprice.services.ProductRetailerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/productRetailer")
@Api
public class ProductRetailerController {
    @Autowired
    ProductRetailerService productRetailerService;

    @ApiOperation(value = "Admin bật tắt trạng thái của product retailer")
    @PutMapping("/toggle/{productRetailerId}")
    public ResponseEntity<Object> toggleEnable(
            @PathVariable("productRetailerId") String strId
    ){
        long productRetailerId;
        try{
            productRetailerId = Long.parseLong(strId);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Id product retailer phải là số nguyên");
        }

        return productRetailerService.toggleEnable(productRetailerId);
    }
}
