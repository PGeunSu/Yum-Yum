package com.reservation.repository;

import com.reservation.entity.message.Message;
import com.reservation.entity.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(User user);

  List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(User user);
}
