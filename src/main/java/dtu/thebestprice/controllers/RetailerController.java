package dtu.thebestprice.controllers;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.request.RetailerForUserRequest;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RetailerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/api/v1/retailer")
public class RetailerController {
    @Autowired
    RetailerService retailerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    RetailerRepository retailerRepository;

    // danh sách nhà bán lẽ
    @ApiOperation(value = "Danh sách nhà bán lẻ")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(retailerService.findAll());
    }

    // xem nhà bán lẽ theo id
    @GetMapping("/{retailerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER','ROLE_SUPER')")
    @ApiOperation(value = "Tìm nhà bán lẽ theo Id")
    public ResponseEntity<Object> findById(
            @PathVariable("retailerId") String strId
    ) {
        long retailerId;
        try {
            retailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("id nhà bán lẽ phải là số nguyên và không được để trống");
        }
        return retailerService.findById(retailerId);
    }


//    // danh sách nhà bán lẽ đang hoạt đông
//    @GetMapping("/isOn")
//    @ApiOperation(value = "Danh sách nhà bán lẽ đang hoạt động")
//    public ResponseEntity<Object> listRetailerIsOn() {
//        return ResponseEntity.ok(
//                retailerRepository
//                        .findByDeleteFlgAndEnableAndApprove(false, true, true)
//                        .stream().map(retailer -> retailerConverter.toRetailerResponse(retailer))
//        );
//    }
//
//    // danh sách nhà bán lẽ không hoạt động
//    @GetMapping("/isOff")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @ApiOperation(value = "Danh sách nhà bán lẽ không hoạt động")
//    public ResponseEntity<Object> listRetailerIsOff() {
//        return ResponseEntity.ok(
//                retailerRepository
//                        .findByDeleteFlgAndEnable(false, false)
//                        .stream().map(retailer -> retailerConverter.toRetailerResponse(retailer))
//        );
//    }

    // page nhà bán lẽ chưa phê duyệt
    @GetMapping("/listRetailerApproveFalse")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Page nhà bán lẽ chưa được xác nhận")
    public ResponseEntity<Object> listRetailerApproveFalse(Pageable pageable) {
        return retailerService.pageRetailerByApprove(false, pageable);
    }

    // page nhà bán lẽ đã được phê duyệt
    @GetMapping("/listRetailerApproveTrue")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Page nhà bán lẽ đã được xác nhận")
    public ResponseEntity<Object> listRetailerApproveTrue(Pageable pageable) {
        return retailerService.pageRetailerByApprove(true, pageable);
    }

    // admin thêm mới nhà bán lẽ
    @PostMapping
    @ApiOperation(value = "Admin Thêm mới nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createRetailer(@RequestBody @Valid RetailerRequest retailerRequest) {
        User user;
        if (retailerRequest.getUserId() == null || retailerRequest.getUserId().equals("")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            user = userRepository.findByUsername(auth.getName()).orElse(null);
        } else {

            long userIdRequest;
            try {
                userIdRequest = Long.parseLong(retailerRequest.getUserId());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("User Id phải là số nguyên");
            }

            user = userRepository
                    .findById(userIdRequest)
                    .orElseThrow(() -> new RuntimeException("Không tồn tại user id"));
        }

        return retailerService.create(retailerRequest, user, false, true, true);
    }

//    // retailer user tạo mới nhà bán lẽ
//    @PostMapping("/retailerCreate")
//    @ApiOperation(value = "role retailer thêm mới nhà bán lẽ")
//    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
//    public ResponseEntity<Object> userRetailerCreateRetailer(@RequestBody @Valid RetailerForUserRequest retailerForUserRequest) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByUsername(auth.getName()).orElse(null);
//        return retailerService.create(retailerForUserRequest,user,false,false,false);
//    }

    // admin cập nhật nhà bán lẽ
    @PutMapping("/{retailerId}")
    @ApiOperation(value = "Admin cập nhật nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateRetailer(
            @RequestBody @Valid RetailerRequest retailerRequest,
            @PathVariable("retailerId") String trRetailerId
    ) {
        long retailerId;
        try {
            if (trRetailerId.trim().equalsIgnoreCase(""))
                throw new RuntimeException("Không được để trống id");
            retailerId = Long.parseLong(trRetailerId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id phải là số nguyên");
        }

        return retailerService.update(retailerRequest, retailerId);
    }

    // retailer cập nhật nhà bán lẽ cả mình
    @PutMapping("/retailerUpdate/{retailerId}")
    @ApiOperation(value = "retailer cập nhật nhà bán lẽ của mình")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    public ResponseEntity<Object> roleRetailerUpdateRetailer(
            @RequestBody @Valid RetailerForUserRequest retailerForUserRequest,
            @PathVariable("retailerId") String trRetailerId
    ) {
        long retailerId;
        try {
            if (trRetailerId.trim().equalsIgnoreCase(""))
                throw new RuntimeException("Không được để trống id");
            retailerId = Long.parseLong(trRetailerId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id phải là số nguyên");
        }
        return retailerService.roleRetailerUpdate(retailerForUserRequest, retailerId);
    }

    // phê duyệt nhà bán lẽ
    @PutMapping("/approveRetailer/{retailerId}")
    @ApiOperation(value = "Admin phê duyệt nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> approveRetailer(
            @PathVariable("retailerId") String strId
    ) throws MessagingException {
        long retailerId;

        try {
            retailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id nhà bán lẽ không hợp lệ");
        }

        return retailerService.approveRetailer(retailerId);
    }

    // amdin khóa hoặc mở khóa nhà bán lẽ
    @PutMapping("/toggle/{retailerId}")
    @ApiOperation(value = "Tắt hoặc mở trạng thái hoạt động của nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> toggleEnable(
            @PathVariable("retailerId") String strId
    ) {
        long retailerId;

        try {
            retailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id nhà bán lẽ không hợp lệ");
        }

        return retailerService.toggleEnable(retailerId);
    }


    // xóa nhà bán lẽ
    @DeleteMapping("/{retailerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER')")
    @ApiOperation(value = "Admin, retailer xóa nhà nhà bán lẽ")
    public ResponseEntity<Object> deleteById(@PathVariable("retailerId") String id) {
        return retailerService.deleteById(id);
    }
}
