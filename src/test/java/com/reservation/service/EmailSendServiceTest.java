package com.reservation.service;

import static org.junit.jupiter.api.Assertions.*;

import com.reservation.config.FeignConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

  @Autowired
  private EmailSendService emailSendService;

  @Test
  public void EmailTest(){
    String response = String.valueOf(emailSendService.sendEmail());
    System.out.println(response);
  }


}