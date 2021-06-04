package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.entities.validator.CustomValidator;
import dtu.thebestprice.payload.request.*;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.RetailerService;
import dtu.thebestprice.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

    @Autowired
    RetailerService retailerService;

    @ApiOperation(value = "Retailer lấy danh sách những cửa hàng mà mình quản lý")
    @GetMapping("/listRetailer")
    @PreAuthorize("hasAuthority('ROLE_RETAILER')")
    public ResponseEntity<Object> findRetailerByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));
        return retailerService.getRetailerByUser(user);
    }

    @PostMapping("/registerRetailerAccount")
    @ApiOperation(value = "Guest đăng ký tài khoản nhà bán lẽ ")
    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_RETAILER')")
    public ResponseEntity<Object> registerRetailerAccount(
            @RequestBody @Valid RetailerForUserRequest retailerRequest
    ) throws MessagingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        User user = userRepository.getOne(userDetails.getId());

        return userService.guestRegisterRetailerAccount(
                user,
                retailerRequest
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/listGuestAccount")
    @ApiOperation(value = "Page tài khoản guest")
    public ResponseEntity<Object> listGuestAccount(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return userService.findByRole(pageable, keyword, ERole.ROLE_GUEST);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/listRetailerAccount")
    @ApiOperation(value = "Page tài khoản retailer")
    public ResponseEntity<Object> listRetailerAccount(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return userService.findByRole(pageable, keyword, ERole.ROLE_RETAILER);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/editProfile")
    @ApiOperation(value = "Cập nhật profile")
    public ResponseEntity<Object> editProfile(
            @RequestBody @Valid UserUpdateRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userService.editProfile(request, userDetails.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/editPassword")
    @ApiOperation(value = "Thay đổi mật khẩu")
    public ResponseEntity<Object> editProfile(
            @RequestBody @Valid PasswordRequest request
    ) {
        if (!CustomValidator.isValid(request.getCurrentPassword()))
            throw new RuntimeException("Mật khẩu hiện tại không được chứa khoảng cách hoặc ký tự tiếng việt");
        if (!CustomValidator.isValid(request.getNewPassword()))
            throw new RuntimeException("Mật khẩu mới không được chứa khoảng cách hoặc ký tự tiếng việt");

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
        if (!CustomValidator.isValid(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập không được chứa khoảng cách hoặc ký tự tiếng việt");
        if (!CustomValidator.isValid(request.getPassword()))
            throw new RuntimeException("Mật khẩu không được chứa khoảng cách hoặc ký tự tiếng việt");

        userService.createNew(request, true, false, ERole.ROLE_GUEST, true);
        return ResponseEntity.ok(new ApiResponse(true, "Đăng ký tài khoản guest thành công"));
    }


//    @PostMapping("/createRetailerAccount")
//    @ApiOperation(value = "Admin tạo tài khoản cho retailer")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<Object> adminCreateRetailerAccount(
//            @RequestBody @Valid UserRetailerRequest request
//    ) {
//        return userService.adminRegisterRetailerAccount(request);
//    }

    // admin chặn hoặc mở chặn người dùng
    @PutMapping("/toggleEnable/{userId}")
    @ApiOperation(value = "Admin chặn hoặc mở chặn tài khoản retailer hoặc guest")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> toggleEnable(
            @PathVariable("userId") String strId
    ) {
        long userId;

        try {
            userId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id người dùng phải là số nguyên");
        }

        return userService.adminToggleEnable(userId);
    }

    // admin chỉnh sửa tài khoản cho guest hoặc retailer
    @PutMapping("/editGuestOrRetailerAccount/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Admin chỉnh sửa tài khoản cho guest hoặc retailer")
    public ResponseEntity<Object> adminEditGuestOrRetailerAccount(
            @PathVariable("userId") String strId,
            @RequestBody @Valid UpdateUserByAdminRequest request
    ) {
        if (!CustomValidator.isValid(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập không được chứa khoảng cách hoặc ký tự tiếng việt");

        long userId;
        long phoneNumber;
        try {
            userId = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id người dùng phải là số nguyên");
        }

        try {
            phoneNumber = Long.parseLong(request.getPhoneNumber());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Số điện thoại không hợp lệ");
        }

        return userService.adminEditGuestOrRetailerAccount(userId, request);
    }

    // admin đổi mật khẩu cho guest hoặc retailer
    @PutMapping("/adminEditPasswordForGuestOrRetailer/{userId}")
    @ApiOperation(value = "Admin cập nhật mật khẩu cho guest hoặc retailer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> adminEditPasswordForGuestOrRetailer(
            @PathVariable("userId") String strUserId,
            @RequestBody @Valid PasswordByAdminRequest request
    ) {

        if (!CustomValidator.isValid(request.getNewPassword()))
            throw new RuntimeException("Mật khẩu không được chứa khoảng cách hoặc ký tự tiếng việt");

        long userId;
        try {
            userId = Long.parseLong(strUserId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id người dùng không được để trống và phải là số nguyên");
        }

        return userService.adminEditPasswordForGuestOrRetailer(userId, request);
    }

    // admin xóa tài khoản retailer hoặc guest
    @DeleteMapping("/deleteGuestOrRetailer/{accountId}")
    @ApiOperation(value = "Admin xóa tài khoản guest hoặc retailer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteGuestOrRetailer(
            @PathVariable("accountId") String accountId
    ) {
        long id;
        try {
            id = Long.parseLong(accountId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id không hợp lệ");
        }

        return userService.deleteGuestOrRetailer(id);
    }

    // admin xem thông tin user
    @GetMapping("/{accountId}")
    @ApiOperation(value = "Admin Xem thông tin user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findById(
            @PathVariable("accountId") String accountId
    ) {
        long id;
        try {
            id = Long.parseLong(accountId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id không hợp lệ");
        }
        return userService.findById(id);
    }


    /*
     * QUYỀN SUPER
     * */
    // quyền supper thêm tài khoản admin
    @PreAuthorize("hasAuthority('ROLE_SUPER')")
    @PostMapping("/createAdminAccount")
    @ApiOperation(value = "root(tài khoản quyền cao nhất) tạo tài khoản admin")
    public ResponseEntity<Object> createAdminAccount(
            @RequestBody @Valid RegisterRequest request
    ) {
        if (!CustomValidator.isValid(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập không được chứa khoảng cách hoặc ký tự tiếng việt");
        if (!CustomValidator.isValid(request.getPassword()))
            throw new RuntimeException("Mật khẩu không được chứa khoảng cách hoặc ký tự tiếng việt");
        userService.createNew(request, true, true, ERole.ROLE_GUEST, true);
        return ResponseEntity.ok(new ApiResponse(true, "Đăng ký tài khoản admin thành công"));
    }


    // quyền supper xóa tài khoản admin,guest,retailer
    @PreAuthorize("hasAuthority('ROLE_SUPER')")
    @DeleteMapping("/superDelete/{userId}")
    @ApiOperation(value = "root(tài khoản quyền cao nhất) xóa tài khoản guest,retailer,admin")
    public ResponseEntity<Object> superDelete(
            @PathVariable("userId") String strId
    ) {
        long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id phải là số nguyên");
        }

        return userService.superDelete(id);
    }
    /*End quyền super*/
}
