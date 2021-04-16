package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.services.ProductService;
import dtu.thebestprice.services.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SearchController {
    @Autowired
    ProductService productService;

    @Autowired
    SearchService searchService;

    @ApiOperation(value = "Lọc sản phẩm")
    @PostMapping(value = "/v1/filter")
    public ResponseEntity<Object> filter(
            @RequestBody FilterRequest filterRequest,
            @PageableDefault(size = 6) Pageable pageable
    ) {

        try {
            return ResponseEntity.ok(productService.filter(filterRequest, pageable));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "Nhập id danh mục không hợp lệ!"),HttpStatus.NOT_FOUND);
        }
    }

}
