package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {
    // phê duyệt tài khoản retailer
    ResponseEntity<Object> approveRetailerAccount(Long userId);

    // danh sách tài khoản yêu cầu phê duyệt
    ResponseEntity<Object> listAccountApproveFalse(Pageable pageable);
}
