package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/product")
@Api
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{productId}")
    @ApiOperation(value = "Tìm sản phẩm theo id")
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
    @ApiOperation(value = "Danh sách sản phẩm theo id danh mục")
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

    @PostMapping
    @ApiOperation(value = "Thêm mới hoặc cập nhật sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> save(@RequestBody @Valid ProductRequest productRequest){
        return productService.save(productRequest);
    }

    @DeleteMapping
    @ApiOperation(value = "Xóa sản phẩm theo id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteById(@RequestParam("id") Long id){
        return productService.deleteById(id);
    }
}
