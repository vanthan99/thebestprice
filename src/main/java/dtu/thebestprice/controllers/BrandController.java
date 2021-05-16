package dtu.thebestprice.controllers;

import dtu.thebestprice.services.BrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    // danh sách tất cả brand
    @GetMapping
    @ApiOperation(value = "Danh sách brand")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    public ResponseEntity<Object> findAllBrandIsEnable(){
        return brandService.findAllBrandIsEnable();
    }

}
