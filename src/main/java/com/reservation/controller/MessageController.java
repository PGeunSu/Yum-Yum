package com.reservation.controller;

import com.reservation.dto.message.MessageCreateDto;
import com.reservation.entity.user.User;
import com.reservation.jwt.config.JwtTokenProvider;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.jwt.dto.UserVo;
import com.reservation.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;

  //쪽지 보내기
  @PostMapping()
  public ResponseEntity<?> sendMessage(@RequestBody MessageCreateDto req, @AuthenticationPrincipal TokenDto sender){
    return ResponseEntity.ok(messageService.createMessage(sender, req));
  }


  //받은 쪽지 전부 확인
  @GetMapping("/receiver")
  public ResponseEntity<?> receiveMessageList( User user){
    return ResponseEntity.ok(messageService.receiveMessageList(user));
  }

  //받은 쪽지 중 한 개 확인
  @GetMapping("/receiver/{id}")
  public ResponseEntity receiveMessage(@PathVariable Long id, User user){
    return ResponseEntity.ok(messageService.receiveMessage(id,user));
  }

  //보낸 쪽지 전부 확인
  @GetMapping("/sender")
  public ResponseEntity<?> sendMessageList(User user){
    return ResponseEntity.ok(messageService.sendMessageList(user));
  }

  //보낸 쪽지 한 개 확인
  @GetMapping("/sender/{id}")
  public ResponseEntity sendMessage(Long id, User user){
    return ResponseEntity.ok(messageService.sendMessage(id, user));
  }

  //받은 쪽지 삭제
  @DeleteMapping("/receiver/{id}")
  public ResponseEntity<?> deleteReceiveMessage(Long id, User user){
    messageService.deleteMessageByReceiver(id, user);
    return ResponseEntity.ok("삭제완료");
  }
  //보낸 쪽지 삭제
  @DeleteMapping("/sender/{id}")
  public ResponseEntity<?> deleteSenderMessage(Long id, User user){
    messageService.deleteMessageBySender(id, user);
    return ResponseEntity.ok("삭제완료");
  }




}
