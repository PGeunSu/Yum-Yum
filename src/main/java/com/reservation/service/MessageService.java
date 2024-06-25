package com.reservation.service;

import static com.reservation.exception.ErrorCode.MESSAGE_NOT_FOUND;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.message.MessageCreateDto;
import com.reservation.dto.message.MessageDto;
import com.reservation.entity.message.Message;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.repository.MessageRepository;
import com.reservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;


  private User getReceiver(MessageCreateDto req) {
      return userRepository.findByName(req.getReceiverName())
          .orElseThrow(() -> new Exception(USER_NOT_FOUND));
  }
  private User getTokenUser(TokenDto sender){
    return userRepository.findById(sender.getId())
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
  }

  //쪽지 생성
  @Transactional
  public MessageDto createMessage(TokenDto sender, MessageCreateDto req){
    User user = getTokenUser(sender);
    User receiver = getReceiver(req);

    Message message = messageRepository.save(
        Message.builder()
            .title(req.getTitle())
            .content(req.getContent())
            .sender(user)
            .receiver(receiver)
            .build()
    );

    return MessageDto.toDto(messageRepository.save(message));
  }

  //수신 메세지 목록
  @Transactional
  public List<MessageDto> receiveMessageList(TokenDto tokenUser){
    User user = userRepository.findById(tokenUser.getId())
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
    return messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(user)
        .stream().map(MessageDto::toDto)
        .collect(Collectors.toList());
  }

  //해당 id 메세지 하나만
  @Transactional
  public MessageDto receiveMessage(Long id, TokenDto receiver){
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new Exception(MESSAGE_NOT_FOUND));
    validateReceiverMessage(receiver, message);

    return MessageDto.toDto(message);
  }

  //수신 메세지 유효성 검사
  private void validateReceiverMessage(TokenDto user, Message message){
    if (!message.getReceiver().isSameUserId(user.getId())){
      throw new Exception(USER_NOT_FOUND);
    }
    if (message.isDeletedByReceiver()){
      throw new Exception(MESSAGE_NOT_FOUND);
    }
  }

  //송신 메세지 목록
  @Transactional
  public List<MessageDto> sendMessageList(TokenDto tokenUser){
    User user = userRepository.findById(tokenUser.getId())
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
    return messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(user)
        .stream().map(MessageDto::toDto)
        .collect(Collectors.toList());
  }

  //해당 id 메세지 하나만
  @Transactional
  public MessageDto sendMessage(Long id, TokenDto sender){
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new Exception(MESSAGE_NOT_FOUND));
    validateSendMessage(sender, message);

    return MessageDto.toDto(message);
  }
  //송신 메세지 유효성 검사
  private void validateSendMessage(TokenDto member, Message message) {
    if (!message.isSender(member)){
      throw new Exception(USER_NOT_FOUND);
    }
    if (message.isDeletedBySender()) {
      throw new Exception(MESSAGE_NOT_FOUND);
    }
  }

  //수신자와 id가 일치하면 삭제 처리(DB 삭제X)
  @Transactional
  public void deleteMessageByReceiver(Long id, TokenDto user){
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new Exception(MESSAGE_NOT_FOUND));
    processDeleteReceiveMessage(user, message);
    checkIsMessageDeletedBySenderAndReceiver(message);
  }

  private void processDeleteReceiveMessage(TokenDto user, Message message){
    if (!message.getReceiver().isSameUserId(user.getId())){
      throw new Exception(USER_NOT_FOUND);
    }
    message.deleteByReceiver();
  }

  //송신자와 id가 일치하면 삭제 처리(DB 삭제X)
  @Transactional
  public void deleteMessageBySender(Long id, TokenDto user){
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new Exception(MESSAGE_NOT_FOUND));
    processDeleteSenderMessage(user, message);
    checkIsMessageDeletedBySenderAndReceiver(message);
  }

  private void processDeleteSenderMessage(TokenDto user, Message message) {
    if (!message.getSender().equals(user)) {
      throw new Exception(USER_NOT_FOUND);
    }
    message.deleteBySender();
  }

  //송신자, 수신자 모두 삭제처리되면 DB 에서 삭제
  private void checkIsMessageDeletedBySenderAndReceiver(Message message) {
    if (message.isDeletedMessage()) {
      messageRepository.delete(message);
    }
  }



}
