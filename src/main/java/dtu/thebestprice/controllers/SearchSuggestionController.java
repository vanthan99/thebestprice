package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.services.SearchSuggestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
public class SearchSuggestionController {
    @Autowired
    SearchSuggestionService searchSuggestionService;

//    @GetMapping(value = "/api/v1/suggestion/{keyword}")
//    public ResponseEntity<Object> search(
//            @PageableDefault(size = 5) Pageable pageable,
//            @PathVariable("keyword") String keyword
//    ) {
//        if (keyword.trim().length() > 1) {
//            return ResponseEntity.ok(searchSuggestionService.findByKeyword(keyword, pageable));
//        }
//        return new ResponseEntity<>(new ApiResponse(false, "Không ký tự trắng và phải nhập trên 3 ký tự"), HttpStatus.LENGTH_REQUIRED);
//    }

    @ApiOperation(value = "Gợi ý sản phẩm tìm kiếm (Sử dụng hibernate search)")
    @GetMapping(value = "/api/v1/suggestion")
    public ResponseEntity<Object> searchV2(
            @RequestParam("keyword") String keyword
    ) {
            return ResponseEntity.ok(searchSuggestionService.findByKeywordV2(keyword.trim()));
    }
}
