package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.SearchTrackingRequest;
import dtu.thebestprice.services.ProductTrackingService;
import dtu.thebestprice.services.SearchTrackingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    ProductTrackingService productTrackingService;

    @Autowired
    SearchTrackingService searchTrackingService;

    @ApiOperation(value = "Product Tracking")
    @PostMapping("/product")
    public ResponseEntity<Object> productTracking(
            @RequestParam("id") Long productId
    ){
        return productTrackingService.productTracking(productId);
    }

    @ApiOperation(value = "Search Tracking")
    @PostMapping("/search")
    public ResponseEntity<Object> searchTracking(
            @RequestBody SearchTrackingRequest searchTrackingRequest
            ){
        return searchTrackingService.searchTracking(searchTrackingRequest);
    }
}
