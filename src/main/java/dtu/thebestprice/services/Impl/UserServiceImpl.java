package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.converters.UserConverter;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.mail.MailTransport;
import dtu.thebestprice.payload.request.*;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.UserRetailerResponse;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RetailerService;
import dtu.thebestprice.services.UserService;
import dtu.thebestprice.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

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

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    MailTransport mailTransport;

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

        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setRole(ERole.ROLE_RETAILER);
            userRepository.save(user);
        }
    }

    @Override
    public User createNew(RegisterRequest request, boolean enable, boolean approve, ERole role, boolean checkValidate) {
        // validate
        if (userRepository.existsByUsernameAndDeleteFlgFalse(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");

        if (userRepository.existsByEmailAndDeleteFlgFalse(request.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        try {
            int phoneNumberTemp = Integer.parseInt(request.getPhoneNumber());
            if (phoneNumberTemp<0)
                throw new RuntimeException("Số điện thoại không thể là số âm");
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
    public ResponseEntity<Object> guestRegisterRetailerAccount(User user, RetailerForUserRequest retailerRequest) throws MessagingException {

//        retailerService.create(retailerRequest, user, false, false, false);

        // kiểm tra xem có bị trùng tên với các nhà bán lẽ khác hay không
        if (retailerRepository.existsByNameAndDeleteFlgFalse(retailerRequest.getName().trim()))
            throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

        // kiểm tra xem có bị trùng logo với các nhà bán lẽ khác hay không?
        if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerRequest.getLogo().trim()))
            throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

        // kiểm tra xem có bị trung homepage với nhà bán lẽ khác hay không?
        if (retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerRequest.getHomePage().trim()))
            throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");


        Retailer retailer = retailerConverter.toEntity(retailerRequest);

        retailer.setApprove(false);
        retailer.setUser(user);
        retailer.setDeleteFlg(false);
        retailer.setEnable(false);
        retailerRepository.save(retailer);

        // gửi email thông báo
        mailTransport.send(user.getEmail(), "Chúng tôi đã tiếp nhận yêu cầu của bạn.Chúng tôi sẽ gửi lại email cho bạn một khi yêu cầu được phê duyệt", "The best price thông báo");

        return ResponseEntity.ok(new ApiResponse(true, "Đăng ký tài khoản nhà bán lẽ thành công. Bạn hãy kiểm tra lại email của mình"));
    }

    @Override
    public ResponseEntity<Object> adminRegisterRetailerAccount(UserRetailerRequest request) {
        if (this.validateWhileCreateUser(request.getUser()) && retailerService.validateWhileCreateRetailer(request.getRetailer())) {

            User user = this.createNew(request.getUser(), true, true, ERole.ROLE_RETAILER, false);

            retailerService.create(request.getRetailer(), user, true, false);


            return ResponseEntity.ok(new ApiResponse(true, "Thêm mới thành công"));
        }

        throw new RuntimeException("Đã xảy ra lõi");
    }

    @Override
    public boolean validateWhileCreateUser(RegisterRequest registerRequest) {
        // validate
        if (userRepository.existsByUsernameAndDeleteFlgFalse(registerRequest.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");

        if (userRepository.existsByEmailAndDeleteFlgFalse(registerRequest.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        try {
            int phoneNumberTemp = Integer.parseInt(registerRequest.getPhoneNumber());
        } catch (Exception e) {
            throw new RuntimeException("Số điên thoại không đúng định dạng.  số điện thoại chỉ bao gồm số từ 0 đến 9");
        }
        return true;
    }

    @Override
    public ResponseEntity<Object> findByRole(Pageable pageable, String keyword, ERole role) {
        Specification<User> condition = Specification.where(
                UserSpecification.userNameContaining(keyword)
                        .or(UserSpecification.fullNameContaining(keyword))
                        .or(UserSpecification.addressContaining(keyword))
                        .or(UserSpecification.emailContaining(keyword))
                        .or(UserSpecification.phoneNumberContaining(keyword))
        ).and(UserSpecification.deleteFlgFalse()).and(UserSpecification.isRole(role));

        Page<User> entityPage = userRepository.findAll(condition, pageable);

        Page<UserRetailerResponse> responsePage = entityPage.map(user -> userConverter.toUserRetailerResponse(user));
        return ResponseEntity.ok(responsePage);

//        if (keyword.trim().equals(""))
//            return ResponseEntity.ok(
//                    userRepository
//                            .findByDeleteFlgFalseAndRole(pageable, role)
//                            .map(user -> userConverter.toUserRetailerResponse(user))
//            );
//
//        return ResponseEntity.ok(
//                userRepository
//                        .findByDeleteFlgFalseAndRoleAndKeyword(pageable, role, keyword)
//                        .map(user -> userConverter.toUserRetailerResponse(user))
//        );
    }

    @Override
    public ResponseEntity<Object> editProfile(UserUpdateRequest request, Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        if (userRepository.existsByIdAndFullNameAndAddressAndPhoneNumberAndDeleteFlgFalse(userId, request.getFullName(), request.getAddress(), request.getPhoneNumber()))
            throw new RuntimeException("Thông tin tài khoản không có thay đổi mới nào");

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
        if (passwordRequest.getCurrentPassword().equals(passwordRequest.getNewPassword()))
            throw new RuntimeException("Mật khẩu không có thay đổi");

        if (passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật mật khẩu thành công!"));
        }
        throw new RuntimeException("Mật khẩu hiện tại không đúng!");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteGuestOrRetailer(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (user.getRole().equals(ERole.ROLE_GUEST) || user.getRole().equals(ERole.ROLE_RETAILER)) {
            // thực hiện xóa...
            user.setDeleteFlg(true);

            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "Xóa tài khoản thành công"));
        }
        return ResponseEntity.status(400).body(new ApiResponse(false, "Admin chỉ được xóa tài khoản guest hoặc retailer"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> superDelete(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (user.getRole().equals(ERole.ROLE_SUPER))
            throw new RuntimeException("Không thể xóa tài khoản của chính mình");

        // thực hiện xóa...
        user.setDeleteFlg(true);

        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa tài khoản thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminEditGuestOrRetailerAccount(long userId, UpdateUserByAdminRequest request) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("không tồn tại người dùng"));

        if (user.getRole().equals(ERole.ROLE_ADMIN) || user.getRole().equals(ERole.ROLE_SUPER))
            throw new RuntimeException("Không đủ quyền để chỉnh sửa tài khoản này");

        if (userRepository.existsByIdAndFullNameAndAddressAndPhoneNumberAndUsernameAndEmailAndDeleteFlgFalse(userId, request.getFullName(), request.getAddress(), request.getPhoneNumber(), request.getUsername(), request.getEmail()))
            throw new RuntimeException("Không tin tài khoản không có thay đổi");

        // check username
        if (userRepository.existsByUsernameAndDeleteFlgFalse(request.getUsername()) && !user.getUsername().equals(request.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã bị trùng");

        // check email
        if (userRepository.existsByEmailAndDeleteFlgFalse(request.getEmail()) && !user.getEmail().equals(request.getEmail()))
            throw new RuntimeException("email đã bị trùng");

        // check phonenumber
        if (userRepository.existsByPhoneNumberAndDeleteFlgFalse(request.getPhoneNumber()) && !user.getPhoneNumber().equals(request.getPhoneNumber()))
            throw new RuntimeException("số điện thoại đã bị trùng");


        User newUser = userConverter.toUser(request, user);

        // nếu thay đổi email mới thì đặt lại thuộc tính approve
        if (!user.getEmail().equals(request.getEmail()))
            newUser.setApprove(false);

        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thông tin cho người dùng thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminEditPasswordForGuestOrRetailer(long userId, PasswordByAdminRequest request) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (user.getRole().equals(ERole.ROLE_ADMIN) || user.getRole().equals(ERole.ROLE_SUPER))
            throw new RuntimeException("Không thể đổi mật khẩu cho tài khoản này");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "Đổi mật khẩu cho người dùng thành công"));
    }

    @Override
    public ResponseEntity<Object> findById(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        return ResponseEntity.ok(userConverter.toUserRetailerResponse(user));
    }

    @Override
    public ResponseEntity<Object> adminToggleEnable(long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Id người dùng không tồn tại"));

        if (user.getRole().equals(ERole.ROLE_ADMIN) || user.getRole().equals(ERole.ROLE_SUPER))
            throw new RuntimeException("Thông thể hành động với tài khoản này");

        String message = "";

        if (user.isEnable()) {
            message = "Chặn người dùng thành công";
        } else {
            message = "Bỏ chặn người dùng thành công";
        }
        user.setEnable(!user.isEnable());
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }
}
