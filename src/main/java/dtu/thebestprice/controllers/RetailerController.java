package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.services.RetailerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/api/v1/retailer")
public class RetailerController {
    @Autowired
    RetailerService retailerService;

    @ApiOperation(value = "Danh sách nhà bán lẻ")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(retailerService.findAll());
    }

    @PostMapping
    @ApiOperation(value = "Thêm hoặc cập nhật nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> save(@RequestBody @Valid RetailerRequest retailerRequest) {
        return retailerService.save(retailerRequest);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Xóa nhà cung cấp")
    public ResponseEntity<Object> deleteById(@RequestParam("id") String id) {
        return retailerService.deleteById(id);
    }
}
