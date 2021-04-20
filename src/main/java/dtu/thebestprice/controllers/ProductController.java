package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.services.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/product")
@Api
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Object> findById(
            @PathVariable(name = "productId") String productId
    ){
        try {
            return ResponseEntity.ok(productService.findById(productId));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/catId/{catId}")
    public ResponseEntity<Object> findByCategoryId(
            Pageable pageable,
            @PathVariable("catId") String catId
    ) {
        try {
            return ResponseEntity.ok(productService.findByCategoryId(pageable,catId));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
