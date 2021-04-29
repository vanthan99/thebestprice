package dtu.thebestprice.controllers;

import dtu.thebestprice.services.ProductTrackingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.PAForUserEnc;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    ProductTrackingService productTrackingService;

    @ApiOperation(value = "Product Tracking")
    @PostMapping("/product")
    public ResponseEntity<Object> productTracking(
            @RequestParam("id") Long productId
    ){
        return productTrackingService.productTracking(productId);
    }
}
