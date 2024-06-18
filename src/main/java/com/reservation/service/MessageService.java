package com.reservation.service;

import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.message.MessageCreateDto;
import com.reservation.dto.message.MessageDto;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.repository.MessageRepository;
import com.reservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  private Optional<User> getReceiver(MessageCreateDto req) {
    if (userRepository.existsByName(req.getReceiverName())) {
      throw new Exception(USER_NOT_FOUND);
    } else {
      return userRepository.findByName(req.getReceiverName());
    }
  }
}
//
//  @Transactional
//  public MessageDto createMessage(User sender, MessageCreateDto req){
//    User receiver = getReceiver(req);
//  }
//
//}
