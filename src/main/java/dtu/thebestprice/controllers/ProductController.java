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
    @ApiOperation(value = "Thêm mới sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductRequest productRequest){
        return productService.create(productRequest);
    }

    @PutMapping("/{productId}")
    @ApiOperation(value = "Cập nhật sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateProduct(
            @RequestBody @Valid ProductRequest productRequest,
            @PathVariable("productId") String strProductId

    ){
        Long productId;
        try {
            if (strProductId.trim().equalsIgnoreCase(""))
                throw new RuntimeException("Không được để trống id");
            productId = Long.parseLong(strProductId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id phải là số nguyên");
        }
        return productService.update(productRequest,productId);
    }


    @DeleteMapping("/{productId}")
    @ApiOperation(value = "Xóa sản phẩm theo id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("productId") String strId){
        long id;
        try{
            id = Long.parseLong(strId);
        }catch (NumberFormatException e){
            throw new NumberFormatException("id phải là số nguyên");
        }
        return productService.deleteById(id);
    }

    // danh sách sản phẩm đã được phê duyệt
    @GetMapping("/approveTrue")
    @ApiOperation(value = "Page sản phẩm đã được phê duyệt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findByApproveTrue(Pageable pageable){
        return productService.findByApprove(true,pageable);
    }

    // danh sách sản phẩm chưa được phê duyệt
    @GetMapping("/approveFalse")
    @ApiOperation(value = "Page sản phẩm chưa được phê duyệt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findByApproveFalse(Pageable pageable){
        return productService.findByApprove(false,pageable);
    }
}
