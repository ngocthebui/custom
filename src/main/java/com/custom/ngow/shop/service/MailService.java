package com.custom.ngow.shop.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;

  public void sendMail(String toEmail, String subject, String content) {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = null;
    try {
      helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(toEmail);
      helper.setSubject(subject);
      helper.setText(content, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Can not send mail");
    }
  }

  public String getTemplateByClassPathResource(String classPathResource) {
    try {
      ClassPathResource resource = new ClassPathResource(classPathResource);
      try (InputStream inputStream = resource.getInputStream()) {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      throw new RuntimeException("Cannot read email template: " + classPathResource, e);
    }
  }
}
