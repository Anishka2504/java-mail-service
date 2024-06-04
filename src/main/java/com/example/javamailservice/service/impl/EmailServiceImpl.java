package com.example.javamailservice.service.impl;

import com.example.javamailservice.entity.ConfirmationToken;
import com.example.javamailservice.entity.User;
import com.example.javamailservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendMail(User user, ConfirmationToken token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUserEmail());
        mailMessage.setSubject("Complete registration!");
        mailMessage.setText("To confirm your account, follow the link: " +
                "http://localhost:8081/confirm?token=" + token.getConfirmationToken());
        javaMailSender.send(mailMessage);
    }

    //send email with HTML template
    @Override
    public void sendMailWithHtmlTemplate(User user, ConfirmationToken token, String subject, String templateName) {
        Context context = new Context();
        context.setVariable("verificationURL", "http://localhost:8081/confirm?token=" + token.getConfirmationToken());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            messageHelper.setTo(user.getUserEmail());
            messageHelper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            messageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Confirmation link was sent to {}", user.getUserEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
