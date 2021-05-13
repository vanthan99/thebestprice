package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.RetailerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/api/v1/retailer")
public class RetailerController {
    @Autowired
    RetailerService retailerService;

    @Autowired
    UserRepository userRepository;

    // danh sách nhà bán lẽ
    @ApiOperation(value = "Danh sách nhà bán lẻ")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(retailerService.findAll());
    }

    // admin thêm mới nhà bán lẽ
    @PostMapping
    @ApiOperation(value = "Admin Thêm mới nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createRetailer(@RequestBody @Valid RetailerRequest retailerRequest) {
        long userIdRequest;
        try {
            userIdRequest = Long.parseLong(retailerRequest.getUserId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("User Id phải là số nguyên");
        }

        User user = userRepository
                .findById(userIdRequest)
                .orElseThrow(() -> new RuntimeException("Không tồn tại user id"));

        return retailerService.create(retailerRequest, user, true, true);
    }

    // admin cập nhật nhà bán lẽ
    @PutMapping("/{retailerId}")
    @ApiOperation(value = "Admin cập nhật nhà bán lẽ")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateREtailer(
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

    // xóa nhà bán lẽ
    @DeleteMapping("/{retailerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Admin xóa nhà nhà bán lẽ")
    public ResponseEntity<Object> deleteById(@PathVariable("retailerId") String id) {
        return retailerService.deleteById(id);
    }

    // page nhà bán lẽ chưa phê duyệt
    @GetMapping("/listRetailerApproveFalse")
    @ApiOperation(value = "Page nhà bán lẽ chưa được xác nhận")
    public ResponseEntity<Object> listRetailerApproveFalse(Pageable pageable) {
        return retailerService.pageRetailerByApprove(false, pageable);
    }

    // page nhà bán lẽ đã được phê duyệt
    @GetMapping("/listRetailerApproveTrue")
    @ApiOperation(value = "Page nhà bán lẽ đã được xác nhận")
    public ResponseEntity<Object> listRetailerApproveTrue(Pageable pageable) {
        return retailerService.pageRetailerByApprove(true, pageable);
    }

    // phê duyệt nhà bán lẽ
    @PutMapping("/approveRetailer/{retailerId}")
    @ApiOperation(value = "Admin phê duyệt nhà bán lẽ")
    public ResponseEntity<Object> approveRetailer(
            @PathVariable("retailerId") String strId
    ) {
        long retailerId;

        try {
            retailerId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id nhà bán lẽ không hợp lệ");
        }

        return retailerService.approveRetailer(retailerId);
    }
}
