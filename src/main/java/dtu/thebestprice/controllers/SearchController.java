package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.services.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class SearchController {
    @Autowired
    ProductService productService;

    @ApiOperation(value = "Lọc sản phẩm")
    @PostMapping(value = "/v1/filter")
    public ResponseEntity<Object> filter(
            @RequestBody FilterRequest filterRequest,
            Pageable pageable
    ){
        return ResponseEntity.ok(productService.filter(filterRequest.getKeyword(), filterRequest.getCatId(), pageable));
    }

}
