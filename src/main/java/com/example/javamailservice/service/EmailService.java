package com.example.javamailservice.service;

import com.example.javamailservice.entity.ConfirmationToken;
import com.example.javamailservice.entity.User;

public interface EmailService {

    void sendMail(User user, ConfirmationToken token);

    void sendMailWithHtmlTemplate(User user, ConfirmationToken token, String subject, String templateName);

}
