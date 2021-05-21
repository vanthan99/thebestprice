package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.ProductRequest;
import dtu.thebestprice.payload.request.RatingRequest;
import dtu.thebestprice.payload.request.product.ProductFullRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.services.RateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/product")
@Api
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    RateService rateService;

    @GetMapping("/{productId}")
    @ApiOperation(value = "Tìm sản phẩm theo id")
    public ResponseEntity<Object> findById(
            @PathVariable(name = "productId") String productId
    ) {
        try {
            return ResponseEntity.ok(productService.findById(productId));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/onlyProduct/{productId}")
    @ApiOperation(value = "Admin, Retailer tìm thông tin sản phẩm theo id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> findProductById(
            @PathVariable(name = "productId") String strId
    ) {
        long productId;
        try {
            productId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Product Id phải là số nguyên");
        }
        return productService.findProductById(productId);
    }

    @GetMapping("/listProductForRetailer")
    @ApiOperation(value = "Page danh sách sản phẩm của retailer")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    public ResponseEntity<Object> listProductForRetailer(
          Pageable pageable
    ) {
        return productService.listProductForRetailer(pageable);
    }

//    @GetMapping("/catId/{catId}")
//    @ApiOperation(value = "Danh sách sản phẩm theo id danh mục")
//    public ResponseEntity<Object> findByCategoryId(
//            Pageable pageable,
//            @PathVariable("catId") String catId
//    ) {
//        try {
//            return ResponseEntity.ok(productService.findByCategoryId(pageable, catId));
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }

    @PostMapping
    @ApiOperation(value = "Thêm mới sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    @PostMapping("/retailerCreateProduct")
    @ApiOperation(value = "Quyền Retailer thêm mới sản phẩm (bao gồm cả giá,retailer)")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    public ResponseEntity<Object> retailerCreateProduct(
            @RequestBody @Valid ProductFullRequest productFullRequest
    ) {
        return productService.retailerCreateProduct(productFullRequest);
    }

    @PutMapping("/{productId}")
    @ApiOperation(value = "Admin, Retailer Cập nhật sản phẩm")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> updateProduct(
            @RequestBody @Valid ProductRequest productRequest,
            @PathVariable("productId") String strProductId
    ) {
        Long productId;
        try {
            if (strProductId.trim().equalsIgnoreCase(""))
                throw new RuntimeException("Không được để trống id");
            productId = Long.parseLong(strProductId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id phải là số nguyên");
        }
        return productService.update(productRequest, productId);
    }


    @DeleteMapping("/{productId}")
    @ApiOperation(value = "Admin, Retailer xóa sản phẩm theo id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> deleteById(@PathVariable("productId") String strId) {
        long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id phải là số nguyên");
        }
        return productService.deleteById(id);
    }

    // danh sách sản phẩm đã được phê duyệt
    @GetMapping("/approveTrue")
    @ApiOperation(value = "Page sản phẩm đã được phê duyệt")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> findByApproveTrue(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.findByApprove(true, pageable);
    }

    // danh sách sản phẩm chưa được phê duyệt
    @GetMapping("/approveFalse")
    @ApiOperation(value = "Page sản phẩm chưa được phê duyệt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findByApproveFalse(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.findByApprove(false, pageable);
    }


    // rating
    @PostMapping("/rating/{productId}")
    @ApiOperation(value = "Người dùng gửi đánh giá (rating) sản phẩm")
    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_RETAILER')")
    public ResponseEntity<Object> ratingTracking(
            @PathVariable("productId") String strProductId,
            @RequestBody @Valid RatingRequest ratingRequest
    ) {
        long productId;
        long rate;

        try {
            productId = Long.parseLong(strProductId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id sản phẩm phải là số nguyên");
        }

        try {
            rate = Long.parseLong(ratingRequest.getRate());
            if (rate < 1 || rate > 5)
                throw new RuntimeException("Chỉ đánh giá từ 1 tới 5 sao");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Rate phải là số nguyên và không được để trống");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        return rateService.rating(userDetails.getId(), rate, productId);
    }

    // amdin khóa hoặc mở khóa san pham
    @PutMapping("/toggle/{productId}")
    @ApiOperation(value = "Tắt hoặc mở trạng thái hoạt động của sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> toggleEnable(
            @PathVariable("productId") String strId
    ) {
        long productId;

        try {
            productId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id sản phẩm không hợp lệ");
        }

        return productService.toggleEnable(productId);
    }


    // amdin phê duyệt sản phẩm
    @PutMapping("/approve/{productId}")
    @ApiOperation(value = "admin phê duyệt của sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminApprove(
            @PathVariable("productId") String strId
    ) {
        long productId;

        try {
            productId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id sản phẩm không hợp lệ");
        }

        return productService.adminApprove(productId);
    }

}
