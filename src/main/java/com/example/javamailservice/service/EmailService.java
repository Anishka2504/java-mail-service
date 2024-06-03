package com.example.javamailservice.service;

import com.example.javamailservice.entity.ConfirmationToken;
import com.example.javamailservice.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.thymeleaf.context.Context;

public interface EmailService {

    void sendMail(User user, ConfirmationToken token);

    void sendMailWithHtmlTemplate(String to, String subject, String templateName, Context context);

}
