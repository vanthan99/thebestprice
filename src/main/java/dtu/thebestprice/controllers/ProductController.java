package dtu.thebestprice.controllers;

import dtu.thebestprice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Object> filter(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "catId", required = false) Long catId,
            Pageable pageable
    ){
        return ResponseEntity.ok(productService.filter(keyword, catId, pageable));
    }
}
