package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.UserConverter;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserConverter userConverter;



    @SneakyThrows
    @Override
    @Transactional
    // phê duyệt tài khoản retailer
    public ResponseEntity<Object> approveRetailerAccount(Long userId) {
        if (!userRepository.existsById(userId))
            throw new RuntimeException("Không tồn tại user");

        User user = userRepository.getOne(userId);

        if (user.isApprove())
            throw new RuntimeException("Tài khoản này đã được phê duyệt trước đó");

        user.setApprove(true);
        userRepository.save(user);


        // gửi mail thông báo
        MimeMessage message = mailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        String htmlMsg =
                "<h3>Chào bạn. Chúng tôi đã xem xét và đã phê duyệt tài khoản của bạn.\n" +
                        "Bạn có thể truy cập tài khoản ngay bây giờ </h3>";
        message.setContent(htmlMsg, "text/html; charset=UTF-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Thông báo tài khoản nhà bán hàng của bạn đã được phê duyệt");
        //-----------------
        mailSender.send(message);


        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt tài khoản thành công"));
    }


    // danh sách các nhà bán lẽ chưa được phê duyệt
    @Override
    public ResponseEntity<Object> listAccountApproveFalse(Pageable pageable) {

        return ResponseEntity.ok(
                userRepository.findByDeleteFlgFalseAndEnableTrueAndApproveFalseOrderByCreatedAtDesc(pageable).map(user -> userConverter.toUserRetailerResponse(user))
        );
    }
}
