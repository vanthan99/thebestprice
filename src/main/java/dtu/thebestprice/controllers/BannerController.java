package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.BannerRequest;
import dtu.thebestprice.services.BannerService;
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
@RequestMapping("/api/v1/banner")
public class BannerController {
    @Autowired
    BannerService bannerService;


    @GetMapping
    @ApiOperation(value = "Danhh sách tất cả banner")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return bannerService.findAll(pageable);
    }

    @GetMapping("/isOn")
    @ApiOperation(value = "Danhh sách banner đang hoạt động")
    public ResponseEntity<Object> listEnableTrue() {
        return bannerService.findByEnable(true);
    }

    @GetMapping("/isOff")
    @ApiOperation(value = "Danhh sách banner đã tắt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> listEnableFalse() {
        return bannerService.findByEnable(false);
    }

    @PostMapping
    @ApiOperation(value = "Tạo mới banner")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createNew(
            @RequestBody @Valid BannerRequest request
    ) {
        return bannerService.createNew(request);
    }

    @PutMapping("/{bannerId}")
    @ApiOperation(value = "Cập nhật banner")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> update(
            @RequestBody @Valid BannerRequest request,
            @PathVariable("bannerId") String strBannerId
    ) {
        long bannerId;
        try {
            bannerId = Long.parseLong(strBannerId);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Banner Id phải là số nguyên");
        }
        return bannerService.update(bannerId, request);
    }

    @DeleteMapping("/{bannerId}")
    @ApiOperation(value = "Xóa banner")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteById(
            @PathVariable("bannerId") String strBannerId
    ) {
        long bannerId;
        try {
            bannerId = Long.parseLong(strBannerId);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Banner Id phải là số nguyên");
        }
        return bannerService.deleteById(bannerId);
    }

    @PutMapping("toggleStatus/{bannerId}")
    @ApiOperation(value = "Thay đổi trạng thái hiện tại của banner sang trạng thái ngược lại(bật - > tắt và tắt -> bật)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> switchToEnable(
            @PathVariable("bannerId") String strBannerId
    ) {
        long bannerId;
        try {
            bannerId = Long.parseLong(strBannerId);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Banner Id phải là số nguyên");
        }
        return bannerService.switchEnable(bannerId);
    }

//    @PutMapping("switchToEnable/{bannerId}")
//    @ApiOperation(value = "Thay đổi trạng thái banner sang bật")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<Object> switchToEnable(
//            @PathVariable("bannerId") String strBannerId
//    ){
//        long bannerId;
//        try{
//            bannerId = Long.parseLong(strBannerId);
//        }catch (NumberFormatException exception){
//            throw new NumberFormatException("Banner Id phải là số nguyên");
//        }
//        return bannerService.switchEnable(bannerId);
//    }
//
//    @PutMapping("switchToDisable/{bannerId}")
//    @ApiOperation(value = "Thay đổi trạng thái banner sang tắt")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<Object> switchToDisable(
//            @PathVariable("bannerId") String strBannerId
//    ){
//        long bannerId;
//        try{
//            bannerId = Long.parseLong(strBannerId);
//        }catch (NumberFormatException exception){
//            throw new NumberFormatException("Banner Id phải là số nguyên");
//        }
//        return bannerService.switchEnable(bannerId);
//    }

}
