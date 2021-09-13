package com.elearning.learning.controller;

import com.elearning.learning.model.EmailMessage;
import com.elearning.learning.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody EmailMessage emailMessage) {

        String response = contactService.sendEmail(emailMessage);
        return response;
    }

}
