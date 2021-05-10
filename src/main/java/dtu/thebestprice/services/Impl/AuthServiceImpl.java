package dtu.thebestprice.services.Impl;

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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private JavaMailSender mailSender;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

//    @PostConstruct
//    public void init() {
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("truongvanthan");
//        request.setPassword("thanthan");
//        request.setEmail("vanthan.ad.it@gmail.com");
//        request.setPhoneNumber("0238472393");
//        request.setFullName("Trương Văn Thân");
//        request.setAddress("Quảng Trị");
//
//        User user = this.toEntity(request);
//        user.setEnable(true);
//        user.setApprove(true);
//        user.setRole(ERole.ROLE_ADMIN);
//
//        userRepository.save(user);
//
//
//        RegisterRequest request2 = new RegisterRequest();
//        request2.setUsername("truongvanthan2");
//        request2.setPassword("thanthan2");
//        request2.setEmail("vanthan.ad.it2@gmail.com");
//        request2.setPhoneNumber("0238472393");
//        request2.setFullName("Trương Văn Thân");
//        request2.setAddress("Quảng Trị");
//
//        User user2 = this.toEntity(request2);
//        user2.setEnable(true);
//        user2.setApprove(true);
//        user2.setRole(ERole.ROLE_RETAILER);
//
//        userRepository.save(user2);
//
//
//    }


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
    public String registerConfirm(String token) {
        if (token == null)
            throw new RuntimeException("Token không tồn tại");

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "Đã xảy ra lỗi";
        }

        User user = verificationToken.getUser();

        if (new Date().getTime() > verificationToken.getExpiryDate().getTime()) {
            verificationTokenRepository.deleteById(verificationToken.getId());
            userRepository.deleteById(user.getId());
            return "Token đã hết hạn";
        }

        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setEnable(true);
            user.setApprove(true);
            userRepository.save(user);

            return "Xác nhận email thành công. hãy trở lại trang web để đăng nhập lại";
        } else {
            user.setEnable(true);
            userRepository.save(user);
            return "Xác nhận email thành công. Chúng tối sẽ gửi 1 email thông báo đến bạn ngay sau khi tài khoản được quản trị viên phê duyệt";
        }
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
                "\r\n" + "http://localhost:8080" + confirmationUrl;
        message.setContent(htmlMsg, "text/html; charset=UTF-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Thông báo từ thebestprice");
//                -----------------
        mailSender.send(message);
    }
}
