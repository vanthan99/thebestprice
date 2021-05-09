package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.converters.UserConverter;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.*;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RetailerService;
import dtu.thebestprice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserConverter userConverter;

    @Autowired
    RetailerService retailerService;

    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    PasswordEncoder passwordEncoder;


//    @SneakyThrows
//    @Override
//    @Transactional
//    // phê duyệt tài khoản retailer
//    public ResponseEntity<Object> approveRetailerAccount(Long userId) {
//        if (!userRepository.existsById(userId))
//            throw new RuntimeException("Không tồn tại user");
//
//        User user = userRepository.getOne(userId);
//
//        if (user.isApprove())
//            throw new RuntimeException("Tài khoản này đã được phê duyệt trước đó");
//
//        user.setApprove(true);
//        userRepository.save(user);
//
//
//        // gửi mail thông báo
//        MimeMessage message = mailSender.createMimeMessage();
//        boolean multipart = true;
//        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
//        String htmlMsg =
//                "<h3>Chào bạn. Chúng tôi đã xem xét và đã phê duyệt tài khoản của bạn.\n" +
//                        "Bạn có thể truy cập tài khoản ngay bây giờ </h3>";
//        message.setContent(htmlMsg, "text/html; charset=UTF-8");
//        helper.setTo(user.getEmail());
//        helper.setSubject("Thông báo tài khoản nhà bán hàng của bạn đã được phê duyệt");
//        //-----------------
//        mailSender.send(message);
//
//
//        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt tài khoản thành công"));
//    }


    // danh sách những tài khoản nhà bán lẽ đã được phê duyệt

    // danh sách các nhà bán lẽ chưa được phê duyệt
    @Override
    public ResponseEntity<Object> listAccountApproveFalse(Pageable pageable) {
        return ResponseEntity.ok(
                userRepository.findByDeleteFlgFalseAndEnableTrueAndApproveFalseOrderByCreatedAtDesc(pageable).map(user -> userConverter.toUserRetailerResponse(user))
        );
    }

    @Override
    @Transactional
    public void updateGuestToRetailer(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        if (!user.getRole().equals(ERole.ROLE_GUEST)) {
            throw new RuntimeException("Tài khoản hiện tại chưa phải là tài khoản guest");
        }

        user.setRole(ERole.ROLE_RETAILER);
        userRepository.save(user);
    }

    @Override
    public User createNew(RegisterRequest request, boolean enable, boolean approve, ERole role,boolean checkValidate) {
        // validate
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        try {
            int phoneNumberTemp = Integer.parseInt(request.getPhoneNumber());
        } catch (Exception e) {
            throw new RuntimeException("Số điên thoại không đúng định dạng.  số điện thoại chỉ bao gồm số từ 0 đến 9");
        }

        // xử lý logic
        User user = userConverter.toUser(request);

        user.setRole(role);
        user.setEnable(enable);
        user.setApprove(approve);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> guestRegisterRetailerAccount(User user, RetailerRequest retailerRequest) {

        retailerService.create(retailerRequest,user,false,true);

        return ResponseEntity.ok(new ApiResponse(true,"Đăng ký tài khoản retailer thành công. hãy đợi quản trị viên phê duyệt"));
    }

    @Override
    public ResponseEntity<Object> adminRegisterRetailerAccount(UserRetailerRequest request) {
        if (this.validateWhileCreateUser(request.getUser()) && retailerService.validateWhileCreateRetailer(request.getRetailer())){

            User user = this.createNew(request.getUser(),true,true,ERole.ROLE_RETAILER,false);

            retailerService.create(request.getRetailer(),user,true,false);


            return ResponseEntity.ok(new ApiResponse(true,"Thêm mới thành công"));
        }

        throw new RuntimeException("Đã xảy ra lõi");
    }

    @Override
    public boolean validateWhileCreateUser(RegisterRequest registerRequest) {
        // validate
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");

        if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        try {
            int phoneNumberTemp = Integer.parseInt(registerRequest.getPhoneNumber());
        } catch (Exception e) {
            throw new RuntimeException("Số điên thoại không đúng định dạng.  số điện thoại chỉ bao gồm số từ 0 đến 9");
        }
        return true;
    }

    @Override
    public ResponseEntity<Object> findByRole(Pageable pageable, ERole role) {
        return ResponseEntity.ok(
                userRepository
                        .findByDeleteFlgFalseAndEnableTrueAndApproveTrueAndRole(pageable, role)
                        .map(user -> userConverter.toUserRetailerResponse(user))
        );
    }

    @Override
    public ResponseEntity<Object> editProfile(UserUpdateRequest request, Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        User newUser = userConverter.toUser(request, user);
        userRepository.save(newUser);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thông tin cá nhân thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updatePassword(PasswordRequest passwordRequest, Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        if (passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật mật khẩu thành công!"));
        }
        throw new RuntimeException("Mật khẩu hiện tại không đúng!");
    }

}
