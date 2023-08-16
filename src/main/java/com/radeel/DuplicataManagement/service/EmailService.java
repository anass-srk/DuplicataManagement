package com.radeel.DuplicataManagement.service;


import java.util.Date;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Service
public class EmailService {

  private JavaMailSender sender = new JavaMailSenderImpl();
  
  void send(String content,@Email String email) throws MessagingException{
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
    helper.setSubject("Confirm you email address");
    helper.setFrom("equipment@supervision.com");
    helper.setTo(email);
    helper.setSentDate(new Date());
    helper.setText(content,true);
    sender.send(message);
  }
  public void sendConfirmationEmail(@NotNull String link,@Email String email) throws MessagingException{
    String content = "<html>"
    + "<head>"
    + "<style>"
    + "h1 { color: #333; font-family: 'Arial', sans-serif; }"
    + "p { color: #555; font-family: 'Arial', sans-serif; font-size: 16px; }"
    + "a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 4px; font-family: 'Arial', sans-serif; font-size: 16px; }"
    + "</style>"
    + "</head>"
    + "<body>"
    + "<h1>Confirmation Email</h1>"
    + "<p>Thank you for registering with our application.</p>"
    + "<p>Please click the button below to confirm your Eamil:</p>"
    + "<a href=\"" + link + "\">Confirm Registration</a>"
    + "</body>"
    + "</html>";
    send(content, email);
  }
}
