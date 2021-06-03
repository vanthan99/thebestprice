package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.price.PriceRequest;
import dtu.thebestprice.payload.request.price.PriceRetailerRequest;
import dtu.thebestprice.payload.request.price.ProductRetailerRequest;
import dtu.thebestprice.services.PriceService;
import dtu.thebestprice.services.ProductRetailerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/price")
public class PriceController {
    @Autowired
    PriceService priceService;

    @Autowired
    ProductRetailerService productRetailerService;

    @ApiOperation(value = "Admin bật tắt trạng thái của product retailer")
    @PutMapping("/toggle/{productRetailerId}")
    public ResponseEntity<Object> toggleEnable(
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id product retailer phải là số nguyên");
        }

        return productRetailerService.toggleEnable(productRetailerId);
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    @ApiOperation(value = "Thông tin giá của theo sản phẩm")
    public ResponseEntity<Object> adminGetPriceByProduct(
            @PathVariable("productId") String strId
    ) {
        long productId;
        try {
            productId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của sản phẩm phải là số nguyên");
        }
        return priceService.adminGetPriceByProduct(productId);
    }

    // nhà bán lẽ cập nhật giá cho sản phẩm của mình
    @ApiOperation(value = "Nhà bán lẽ cập nhật giá bán cho sản phẩm của mình")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    @PutMapping("/retailerUpdatePrice/{productRetailerId}")
    public ResponseEntity<Object> retailerUpdatePrice(
            @RequestBody @Valid PriceRequest priceRequest,
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của product retailer là số nguyên");
        }
        return priceService.retailerUpdatePrice(productRetailerId, priceRequest);
    }

    // admin phê duyệt giá
    @ApiOperation(value = "Admin phê duyệt giá")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/adminApprovePrice/{productRetailerId}")
    public ResponseEntity<Object> adminApprovePrice(
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id giá phải là số nguyên");
        }
        return priceService.adminApprovePrice(productRetailerId);
    }

    // admin câp nhất giá cho mọi sản phẩm
    @ApiOperation(value = "Admin cập nhật giá cho mọi sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/adminUpdatePrice/{productRetailerId}")
    public ResponseEntity<Object> adminUpdatePrice(
            @RequestBody @Valid PriceRequest priceRequest,
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của product retailer là số nguyên");
        }
        return priceService.adminUpdatePrice(productRetailerId, priceRequest);
    }

    // retailer or admin chỉnh sửa product retailer
    @ApiOperation(value = "Admin, Retailer chỉnh sửa Product_Retailer")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    @PutMapping("/editProductRetailer/{productRetailerId}")
    public ResponseEntity<Object> updateProductRetailer(
            @RequestBody @Valid ProductRetailerRequest productRetailerRequest,
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("productRetailerId phải là số nguyên");
        }

        return productRetailerService.update(productRetailerId,productRetailerRequest);

    }


    // danh sách giá chưa approve
    @ApiOperation(value = "Page giá chưa được phê duyệt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/approveFalse")
    public ResponseEntity<Object> approveFalse(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return productRetailerService.findByApprove(false, pageable);
    }

    //retailer thêm mới 1 productretailer_price
    @ApiOperation(value = "Retailer thêm mới giá")
    @PostMapping("/retailerCreateNewPrice/{productId}")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    public ResponseEntity<Object> retailerCreateNewPrice(
            @PathVariable("productId") String strPId,
            @RequestBody @Valid PriceRetailerRequest priceRequest
    ) {
        long productId;
        try {
            productId = Long.parseLong(strPId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của sản phẩm phải là số nguyên");
        }

        return priceService.retailerCreateNewPrice(productId, priceRequest);
    }

    // retailer xóa thông tin xóa product_retailer
    @ApiOperation(value = "Retailer xóa 1 sản phẩm mà họ đang kinh doanh")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    @DeleteMapping("/retailerDelete/{productRetailerId}")
    public ResponseEntity<Object> retailerDelete(
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("product retailer id phải là số nguyên");
        }
        return priceService.retailerDelete(productRetailerId);
    }

    //admin thêm mới 1 productretailer_price
    @ApiOperation(value = "Admin thêm mới giá")
    @PostMapping("/adminCreateNewPrice/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminCreateNewPrice(
            @PathVariable("productId") String strPId,
            @RequestBody @Valid PriceRetailerRequest priceRetailerRequest
    ) {
        long productId;
        try {
            productId = Long.parseLong(strPId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của sản phẩm phải là số nguyên");
        }

        return priceService.adminCreateNewPrice(productId, priceRetailerRequest);
    }

    // admin xóa thông tin xóa product_retailer
    @ApiOperation(value = "Admin xóa 1 product_retailer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/adminDelete/{productRetailerId}")
    public ResponseEntity<Object> AdminDelete(
            @PathVariable("productRetailerId") String strId
    ) {
        long productRetailerId;
        try {
            productRetailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("product retailer id phải là số nguyên");
        }
        return priceService.adminDelete(productRetailerId);
    }

    // retailer xem danh sách giá của mình
    @ApiOperation(value = "Retailer xem danh sách giá của mình")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    @GetMapping("/getPagePriceForRetailer")
    public ResponseEntity<Object> getPagePriceForRetailer(
           Pageable pageable
    ) {
        return priceService.getPagePriceForRetailer(pageable);
    }

}
