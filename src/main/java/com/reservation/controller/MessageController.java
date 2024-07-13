package com.reservation.controller;

import com.reservation.dto.message.MessageCreateDto;
import com.reservation.dto.message.MessageDto;
import com.reservation.entity.user.User;
import com.reservation.service.MessageService;
import com.reservation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;

  //쪽지 보내기
  @PostMapping()
  public ResponseEntity<?> sendMessage(@RequestBody MessageCreateDto req, Authentication auth){
    User sender = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.createMessage(sender, req));
  }

  //받은 쪽지 전부 확인
  @GetMapping("/receiver")
  public ResponseEntity<List<MessageDto>> receiveMessageList(Authentication auth){
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.receiveMessageList(user));
  }

  //받은 쪽지 중 한 개 확인
  @GetMapping("/receiver/{id}")
  public ResponseEntity<MessageDto> receiveMessage(@PathVariable Long id, Authentication auth){
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.receiveMessage(id,user));
  }

  //보낸 쪽지 전부 확인
  @GetMapping("/sender")
  public ResponseEntity<List<MessageDto>> sendMessageList(Authentication auth){
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.sendMessageList(user));
  }

  //보낸 쪽지 한 개 확인
  @GetMapping("/sender/{id}")
  public ResponseEntity<MessageDto> sendMessage(@PathVariable Long id, Authentication auth){
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.sendMessage(id, user));
  }

  //받은 쪽지 삭제
  @DeleteMapping("/receiver/{id}")
  public ResponseEntity<String> deleteReceiveMessage(@PathVariable Long id, Authentication auth){
    User user = userService.getUser(auth.getName());
    messageService.deleteMessageByReceiver(id, user);
    return ResponseEntity.ok("삭제완료");
  }
  //보낸 쪽지 삭제
  @DeleteMapping("/sender/{id}")
  public ResponseEntity<String> deleteSenderMessage(@PathVariable Long id,Authentication auth){
    User user = userService.getUser(auth.getName());
    messageService.deleteMessageBySender(id, user);
    return ResponseEntity.ok("삭제완료");
  }




}
