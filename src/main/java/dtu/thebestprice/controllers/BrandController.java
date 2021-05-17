package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.brand.BrandRequest;
import dtu.thebestprice.services.BrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    // danh sách tất cả brand đang hoạt động
    @GetMapping
    @ApiOperation(value = "Danh sách brand đang hoạt động")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> findAllBrandIsEnable() {
        return brandService.findAllBrandByEnable(true);
    }

    // danh sách những brand bị khóa
    @GetMapping("/disable")
    @ApiOperation(value = "Danh sách brand đang hoạt động")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findAllBrandIsDisable() {
        return brandService.findAllBrandByEnable(false);
    }

    // thêm mới brand
    @PostMapping
    @ApiOperation(value = "Admin thêm mới brand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminCreateBrand(
            @RequestBody @Valid BrandRequest brandRequest
    ) {
        return brandService.adminCreateBrand(brandRequest);
    }

    // chỉnh sửa brand
    @PutMapping("/edit/{brandId}")
    @ApiOperation(value = "Admin thêm mới brand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminUpdateBrand(
            @RequestBody @Valid BrandRequest brandRequest,
            @PathVariable("brandId") String strBrandId
    ) {
        return brandService.adminUpdateBrand(strBrandId, brandRequest);
    }

    // bật tắt trạng thái brand
    @PutMapping("/toggleEnable/{brandId}")
    @ApiOperation(value = "Admin bật tắt trạng thái hoạt động của nhà sản xuất")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminToggleBrand(
            @PathVariable("brandId") String strBrandId
    ) {
        return brandService.adminToggleBrand(strBrandId);
    }

    // xóa brand
    @DeleteMapping("/{brandId}")
    @ApiOperation(value = "Admin xóa nhà sản xuất")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminDeleteBrand(
            @PathVariable("brandId") String strBrandId
    ) {
        return brandService.adminDeleteBrand(strBrandId);
    }
}
