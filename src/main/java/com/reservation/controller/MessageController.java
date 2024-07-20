package com.reservation.controller;

import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.message.MessageCreateDto;
import com.reservation.dto.message.MessageDto;
import com.reservation.entity.user.User;
import com.reservation.exception.ErrorCode;
import com.reservation.exception.Exception;
import com.reservation.repository.MessageRepository;
import com.reservation.repository.UserRepository;
import com.reservation.service.MessageService;
import com.reservation.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;


  @GetMapping()
  public String sendMessage(Model model) {
    model.addAttribute("messageCreateDto", new MessageCreateDto());

    return "message/send";
  }

  @PostMapping()
  public String sendMessage(@Valid MessageCreateDto req, Authentication auth,
      BindingResult bindingResult, Model model) {

    if (!userRepository.existsByName(req.getReceiverName())) {
      bindingResult.addError(new FieldError("form", "receiverName", "수신자가 존재하지 않습니다."));
    }
    if (bindingResult.hasErrors()) {
      return "message/send";
    }
    User sender = userService.getUser(auth.getName());
    model.addAttribute("sender", sender);
    messageService.createMessage(sender, req);

    return "redirect:/users/myPage";
  }

  //받은 쪽지 전부 확인
  @GetMapping("/receiver")
  public ResponseEntity<List<MessageDto>> receiveMessageList(Authentication auth) {
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.receiveMessageList(user));
  }

  //받은 쪽지 중 한 개 확인
  @GetMapping("/receiver/{id}")
  public ResponseEntity<MessageDto> receiveMessage(@PathVariable Long id, Authentication auth) {
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.receiveMessage(id, user));
  }

  //보낸 쪽지 전부 확인
  @GetMapping("/sender")
  public ResponseEntity<List<MessageDto>> sendMessageList(Authentication auth) {
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.sendMessageList(user));
  }

  //보낸 쪽지 한 개 확인
  @GetMapping("/sender/{id}")
  public ResponseEntity<MessageDto> sendMessage(@PathVariable Long id, Authentication auth) {
    User user = userService.getUser(auth.getName());
    return ResponseEntity.ok(messageService.sendMessage(id, user));
  }

  //받은 쪽지 삭제
  @DeleteMapping("/receiver/{id}")
  public ResponseEntity<String> deleteReceiveMessage(@PathVariable Long id, Authentication auth) {
    User user = userService.getUser(auth.getName());
    messageService.deleteMessageByReceiver(id, user);
    return ResponseEntity.ok("삭제완료");
  }

  //보낸 쪽지 삭제
  @DeleteMapping("/sender/{id}")
  public ResponseEntity<String> deleteSenderMessage(@PathVariable Long id, Authentication auth) {
    User user = userService.getUser(auth.getName());
    messageService.deleteMessageBySender(id, user);
    return ResponseEntity.ok("삭제완료");
  }


}
