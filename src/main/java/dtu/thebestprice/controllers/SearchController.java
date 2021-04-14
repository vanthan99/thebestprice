package dtu.thebestprice.controllers;

import dtu.thebestprice.services.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SearchController {
    @Autowired
    ProductService productService;

    @ApiOperation(value = "Lọc sản phẩm")
    @GetMapping(value = "/v1/filter")
    public ResponseEntity<Object> filter(
            @ApiParam(value = "Từ khóa cần lọc") @RequestParam(name = "keyword", required = false) String keyword,
            @ApiParam(value = "Mã danh mục cần lọc") @RequestParam(name = "catId", required = false) Long catId,
            Pageable pageable
    ){

        return ResponseEntity.ok(productService.filter(keyword, catId, pageable));
    }



}
