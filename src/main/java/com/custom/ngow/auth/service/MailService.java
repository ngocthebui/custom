package com.custom.ngow.auth.service;

import com.custom.ngow.auth.enity.Otp;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private static final String TIME_OTP_EXPIRED = "5";

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${frontend.verify_account_path}")
    private String verifyAccountPath;

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
        String subject = "Your OTP Code";
        String otp = otpService.createOTPWithEmail(toEmail);
        String content = getEmailOTPTemplate(toEmail, otp);

        sendMail(toEmail, subject, content);
        log.info("Send OTP to {}", toEmail);
    }

    private String getEmailOTPTemplate(String toEmail, String otp) {
        String template = getTemplateByClassPathResource("template/otp_email_template.html");

        template = template.replace("{{email}}", toEmail);
        template = template.replace("{{otp}}", otp);
        template = template.replace("{{time}}", TIME_OTP_EXPIRED);

        return template;
    }

    private String getTemplateByClassPathResource(String classPathResource) {
        ClassPathResource resource = new ClassPathResource(classPathResource);
        String template;
        try {
            Path path = resource.getFile().toPath();
            template = Files.readString(path);
        } catch (IOException e) {
            throw new ForwardException(ErrorCode.E500100, "Can not get email template");
        }

        return template;
    }

    public void sendActiveAccount(String username, String toEmail) {
        String subject = "Verify your Account";

        String otp = otpService.createOTPWithEmail(toEmail);
        String url = frontendUrl + verifyAccountPath + "/" + username + "?otp=" + otp;

        String content = getEmailVerifyAccountTemplate(toEmail, url);

        sendMail(toEmail, subject, content);
        log.info("Send mail verify account to {}", toEmail);
    }

    private String getEmailVerifyAccountTemplate(String toEmail, String url) {
        String template = getTemplateByClassPathResource("template/verify_account_email_template.html");

        template = template.replace("{{email}}" , toEmail);
        template = template.replace("{{verification_link}}", url);
        template = template.replace("{{time}}", TIME_OTP_EXPIRED);

        return template;
    }

}
