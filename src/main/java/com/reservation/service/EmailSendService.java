package com.reservation.service;

import com.reservation.mailgun.MailgunClient;
import com.reservation.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

  private final MailgunClient mailgunClient;

  public String sendEmail(){
    SendMailForm form = SendMailForm.builder()
        .from("zerobase-test@email.com")
        .to("geunsu902@gmail.com")
        .subject("Test Email")
        .text("text")
        .build();

    return mailgunClient.sendEmail(form).getBody();
  }

}
