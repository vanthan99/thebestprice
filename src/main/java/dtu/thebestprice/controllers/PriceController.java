package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.price.RetailerUpdatePriceRequest;
import dtu.thebestprice.services.PriceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/hotDeal")
public class PriceController {
    @Autowired
    PriceService priceService;

    // nhà bán lẽ cập nhật giá cho sản phẩm của mình
    @ApiOperation(value = "Nhà bán lẽ cập nhật giá bán cho sản phẩm của mình")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    @PutMapping("/retailerUpdatePrice")
    public ResponseEntity<Object> retailerUpdatePrice(
            @RequestBody @Valid RetailerUpdatePriceRequest priceRequest
    ) {
        return priceService.retailerUpdatePrice(priceRequest);
    }

    // admin phê duyệt giá
    @ApiOperation(value = "Admin phê duyệt giá")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/adminApprovePrice/{priceId}")
    public ResponseEntity<Object> adminApprovePrice(
            @PathVariable("priceId") String strId
    ) {
        long priceId;
        try {
            priceId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id giá phải là số nguyên");
        }
        return priceService.adminApprovePrice(priceId);
    }


    // admin câp nhất giá cho mọi sản phẩm
    @ApiOperation(value = "Admin cập nhật giá cho mọi sản phẩm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/adminUpdatePrice")
    public ResponseEntity<Object> adminUpdatePrice(
            @RequestBody @Valid RetailerUpdatePriceRequest priceRequest
    ) {
        return priceService.adminUpdatePrice(priceRequest);
    }
}
