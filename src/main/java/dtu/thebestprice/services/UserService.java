package dtu.thebestprice.services;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface UserService {
    // phê duyệt tài khoản retailer
//    ResponseEntity<Object> approveRetailerAccount(Long userId);

    // danh sách tài khoản chưa được phê duyệt
    ResponseEntity<Object> listAccountApproveFalse(Pageable pageable);

    // danh sách tài khoản đã được phê duyệt
//    ResponseEntity<Object> listAccountApproveTrue(Pageable pageable);

    void updateGuestToRetailer(Long userId);

    // tạo user
    User createNew(RegisterRequest request, boolean enable, boolean approve, ERole role, boolean checkValidate);

    // guest Đăng ký tài khoản retailer
    ResponseEntity<Object> guestRegisterRetailerAccount(User user, RetailerForUserRequest retailerRequest) throws MessagingException;

    // admin đăng ký tài khoản retailer
    ResponseEntity<Object> adminRegisterRetailerAccount(UserRetailerRequest request);


    boolean validateWhileCreateUser(RegisterRequest registerRequest);

    // page người dùng guest hoặc retailer
    ResponseEntity<Object> findByRole(Pageable pageable,String keyword, ERole role);

    // sửa profile
    ResponseEntity<Object> editProfile(UserUpdateRequest request, Long userId);

    ResponseEntity<Object> updatePassword(PasswordRequest passwordRequest, Long userId);

    // Admin xóa tài khoản guest hoặc retailer
    ResponseEntity<Object> deleteGuestOrRetailer(long id);

    // root xoá tài khoản admin,guest,retailer
    ResponseEntity<Object> superDelete(long id);

    ResponseEntity<Object> adminEditGuestOrRetailerAccount(long userId, UpdateUserByAdminRequest request);

    // admin sửa mật khẩu cho guest hoặc retailer
    ResponseEntity<Object> adminEditPasswordForGuestOrRetailer(long userId, PasswordByAdminRequest request);

    // thông tin user theo id
    ResponseEntity<Object> findById(long id);

    ResponseEntity<Object> adminToggleEnable(long userId);
}
