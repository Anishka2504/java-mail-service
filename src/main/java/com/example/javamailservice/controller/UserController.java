package com.example.javamailservice.controller;

import com.example.javamailservice.entity.User;
import com.example.javamailservice.service.EmailService;
import com.example.javamailservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @RequestMapping(value = "/confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAccount(@RequestParam("token") String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmailWithHtml(@RequestBody User user) {
        emailService.sendMailWithHtmlTemplate(user, "Email confirmation", "email-template");
        return ResponseEntity.ok("Verify email by the link on your email");
    }

}
