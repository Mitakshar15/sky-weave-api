package org.skyweave.service.api.utils.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActivationEmailSender {

  @Autowired
  private JavaMailSender mailSender;

  public boolean sendEmail(String to, String subject, String text) throws MessagingException {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom("from@demomailtrap.com");
      helper.setTo("work.mitakshar@gmail.com");
      helper.setSubject(subject);
      helper.setText(text);
      helper.setSentDate(Date.from(Instant.now()));
      mailSender.send(message);
      return true;
    } catch (MessagingException e) {
      throw new MessagingException("Failed to send activation email", e);
    }

  }

}
