package com.example.javamailservice.service.impl;

import com.example.javamailservice.annotation.LogExecution;
import com.example.javamailservice.entity.ConfirmationToken;
import com.example.javamailservice.entity.User;
import com.example.javamailservice.repository.ConfirmationTokenRepository;
import com.example.javamailservice.repository.UserRepository;
import com.example.javamailservice.service.EmailService;
import com.example.javamailservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ConfirmationTokenRepository tokenRepository;
    private EmailService emailService;

    @Override
    @LogExecution
    public ResponseEntity<?> saveUser(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            return ResponseEntity.badRequest().body("Error: email is already in use");
        }
        userRepository.save(user);

        ConfirmationToken token = new ConfirmationToken(user);
        tokenRepository.save(token);
//        emailService.sendMail(user, token);
        emailService.sendMailWithHtmlTemplate(user, token, "Confirm email", "email-template");
        System.out.printf("Confirmation token: %s%n", token.getConfirmationToken());
        return ResponseEntity.ok("Verify email by the link on your email");
    }

    @Override
    @LogExecution
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = tokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userRepository.findByUserEmailIgnoreCase(token.getUser().getUserEmail());
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully");
        }
        return ResponseEntity.badRequest().body("Error. Couldn't verify email");
    }
}
