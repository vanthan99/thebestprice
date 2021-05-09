package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.*;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/registerRetailerAccount")
    @ApiOperation(value = "Guest đăng ký tài khoản nhà bán lẽ ")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<Object> registerRetailerAccount(
            @RequestBody @Valid RetailerRequest retailerRequest
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        if (!userDetails.isGuest()) {
            throw new RuntimeException("Chỉ có tài khoản guest mới được đăng ký tài khoản nhà cung cấp");
        }
        return userService.guestRegisterRetailerAccount(
                userRepository.getOne(userDetails.getId()),
                retailerRequest
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/listGuestAccount")
    @ApiOperation(value = "Page tài khoản guest")
    public ResponseEntity<Object> listGuestAccount(Pageable pageable) {
        return userService.findByRole(pageable, ERole.ROLE_GUEST);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/listRetailerAccount")
    @ApiOperation(value = "Page tài khoản retailer")
    public ResponseEntity<Object> listRetailerAccount(Pageable pageable) {
        return userService.findByRole(pageable, ERole.ROLE_RETAILER);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/editProfile")
    @ApiOperation(value = "Cập nhật profile")
    public ResponseEntity<Object> editProfile(
            @RequestBody @Valid UserUpdateRequest request,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        return userService.editProfile(request, myUserDetails.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/editPassword")
    @ApiOperation(value = "Thay đổi mật khẩu")
    public ResponseEntity<Object> editProfile(
            @RequestBody @Valid PasswordRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userService.updatePassword(request, userDetails.getId());
    }

    @PostMapping("/createGuestAccount")
    @ApiOperation(value = "Admin tạo tài khoản cho guest")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminCreateGuestAccount(
            @RequestBody @Valid RegisterRequest request
    ) {
        userService.createNew(request, true, true, ERole.ROLE_GUEST, true);
        return ResponseEntity.ok(new ApiResponse(true, "Đăng ký tài khoản guest thành công"));
    }


    @PostMapping("/createRetailerAccount")
    @ApiOperation(value = "Admin tạo tài khoản cho retailer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminCreateRetailerAccount(
            @RequestBody @Valid UserRetailerRequest request
    ) {
        return userService.adminRegisterRetailerAccount(request);
    }


//    @PostMapping("/approveRetailer")
//    @ApiOperation(value = "Phê duyệt tài khoản retailer")
//    public ResponseEntity<Object> approveRetailerAccount(
//            @RequestParam(value = "id",required = false) String strRetailerId
//    ){
//        Long retailerId = null;
//        try {
//            retailerId = Long.parseLong(strRetailerId);
//        }catch (Exception e){
//            throw new RuntimeException("Id không đúng định dạng");
//        }
//
//        return userService.approveRetailerAccount(retailerId);
//
//    }


//    @GetMapping("/listRetailerAccountApproveFalse")
//    @ApiOperation(value = " Danh sách những tài khoản nhà bán lẽ đợi phê duyệt")
//    public ResponseEntity<Object> listRetailerAccountApproveFalse(Pageable pageable){
//        return userService.listAccountApproveFalse(pageable);
//    }
//
//    @GetMapping("/listRetailerAccountApproveTrue")
//    @ApiOperation(value = " Danh sách những tài khoản nhà bán lẽ đã phê duyệt")
//    public ResponseEntity<Object> listRetailerAccountApproveTrue(Pageable pageable){
//        return userService.listAccountApproveTrue(pageable);
//    }

}
