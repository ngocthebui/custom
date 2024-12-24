package com.custom.ngow.auth.service;

import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final OtpService otpService;

    private void sendMail(String toEmail, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new ForwardException(ErrorCode.E500100, "Can not send mail");
        }
    }

    public void sendOtp(String toEmail) {
        String subject = "OTP Code";
        String otp = otpService.createOTPWithEmail(toEmail);
        String content = getEmailOTPTemplate(toEmail, otp, "5");

        sendMail(toEmail, subject, content);
        log.info("Send OTP to {}", toEmail);
    }

    private String getEmailOTPTemplate(String email, String otp, String time) {
        ClassPathResource resource = new ClassPathResource("template/otp_email_template.html");
        String template;
        try {
            Path path = resource.getFile().toPath();
            template = Files.readString(path);
        } catch (IOException e) {
            throw new ForwardException(ErrorCode.E500100, "Can not get email template");
        }

        template = template.replace("{{email}}", email);
        template = template.replace("{{otp}}", otp);
        template = template.replace("{{time}}", time);

        return template;
    }

}
