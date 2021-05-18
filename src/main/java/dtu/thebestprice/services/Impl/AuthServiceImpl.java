package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.UserConverter;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.VerificationToken;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.repositories.VerificationTokenRepository;
import dtu.thebestprice.services.AuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserConverter userConverter;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @SneakyThrows
    @Override
    public ApiResponse register(RegisterRequest registerRequest, ERole eRole) {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");

        if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        try {
            int phoneNumberTemp = Integer.parseInt(registerRequest.getPhoneNumber());
        } catch (Exception e) {
            throw new RuntimeException("Số điên thoại không đúng định dạng.  số điện thoại chỉ bao gồm số từ 0 đến 9");
        }

        User user = toEntity(registerRequest);

        user.setRole(eRole);

        userRepository.save(user);

        confirmRegistration(user);

        return new ApiResponse(true, "Chúng tôi đã gửi thông báo đến email của bạn");

    }

    @Override
    @Transactional
    public ResponseEntity<Object> registerConfirm(String token) {
        if (token == null)
            throw new RuntimeException("Token Trống");

        UUID tepm;
        try {
            tepm = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token không đúng định dạng");
        }

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            throw new RuntimeException("Token không tồn tại");
        }

        User user = verificationToken.getUser();

        if (new Date().getTime() > verificationToken.getExpiryDate().getTime()) {
            verificationTokenRepository.deleteById(verificationToken.getId());
            userRepository.deleteById(user.getId());
            throw new RuntimeException("Token đã hết hạn");
        }

        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setEnable(true);
            user.setApprove(true);
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "Xác nhận email thành công. hãy trở lại trang web để đăng nhập lại"));
        } else {
            user.setEnable(true);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "Xác nhận email thành công. Chúng tối sẽ gửi 1 email thông báo đến bạn ngay sau khi tài khoản được quản trị viên phê duyệt"));
        }
    }

    @Override
    public ResponseEntity<Object> me() {
        Authentication authentication    = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        return ResponseEntity.ok(userConverter.toUserResponse(user));
    }


    private User toEntity(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setAddress(registerRequest.getAddress());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEmail(registerRequest.getEmail());
        return user;
    }

    private void confirmRegistration(User user) throws MessagingException {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(verificationToken.calculateExpiryDate());
        verificationTokenRepository.save(verificationToken);

        String confirmationUrl
                = "/xac-nhan-email?token=" + token;
//        --------------------
        MimeMessage message = mailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        String htmlMsg = "<h3>Hi!</h3>"
                + "<h3>Chào mừng đến với thebestprice. Để hoàn tất quá trình đăng ký, vui lòng nhấn vào đường link bên dưới" +
                "\r\n" + "https://thebestprice.tech" + confirmationUrl;
        message.setContent(htmlMsg, "text/html; charset=UTF-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Thông báo từ thebestprice");
//                -----------------
        mailSender.send(message);
    }
}
