package dtu.thebestprice.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailTransport {
    @Autowired
    JavaMailSender mailSender;

    public void send(String email, String message, String title) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart, "utf-8");
        mimeMessage.setContent(message, "text/html; charset=UTF-8");
        helper.setTo(email);
        helper.setSubject(title);
        mailSender.send(mimeMessage);
    }
}
