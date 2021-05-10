package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.RatingRequest;
import dtu.thebestprice.payload.request.SearchTrackingRequest;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.ProductTrackingService;
import dtu.thebestprice.services.RateService;
import dtu.thebestprice.services.SearchTrackingService;
import dtu.thebestprice.services.StatisticAccessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    ProductTrackingService productTrackingService;

    @Autowired
    SearchTrackingService searchTrackingService;

    @Autowired
    StatisticAccessService statisticAccessService;

    @Autowired
    RateService rateService;

    @ApiOperation(value = "Product Tracking")
    @PostMapping("/product")
    public ResponseEntity<Object> productTracking(
            @RequestParam("id") Long productId
    ) {
        return productTrackingService.productTracking(productId);
    }

    @ApiOperation(value = "Search Tracking")
    @PostMapping("/search")
    public ResponseEntity<Object> searchTracking(
            @RequestBody SearchTrackingRequest searchTrackingRequest
    ) {
        return searchTrackingService.searchTracking(searchTrackingRequest);
    }

    // access tracking
    @PostMapping("/access")
    @ApiOperation(value = "Tracking mỗi khi truy cập vào hệ thống")
    public ResponseEntity<Object> accessTracking() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            // người dùng đã đăng nhập
            return statisticAccessService.save(true);
        } else {
            // người dùng chưa đăng nhập
            return statisticAccessService.save(false);
        }

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
}
