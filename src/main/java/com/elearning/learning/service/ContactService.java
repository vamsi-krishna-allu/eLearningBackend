package com.elearning.learning.service;

import com.elearning.learning.model.EmailMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactService {

    private final JavaMailSender javaMailSender;

    public String sendEmail(EmailMessage emailMessage) {
        try {
            sendmail(emailMessage);
        } catch (Exception e) {
            return null;
        }
        return "SENT SUCCESSFULLY";
    }


    private void sendmail(EmailMessage emailMessage) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("nobooksweb@gmail.com");

        msg.setSubject("from: "+ emailMessage.getMailId() + " subject: " + emailMessage.getSubject());
        msg.setText(emailMessage.getMessage());

        javaMailSender.send(msg);

    }
}
