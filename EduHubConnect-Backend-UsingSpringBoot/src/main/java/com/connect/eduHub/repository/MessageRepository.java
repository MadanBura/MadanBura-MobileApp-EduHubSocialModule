package com.connect.eduHub.repository;

import com.connect.eduHub.model.Message;
import com.connect.eduHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);
    List<Message> findByReceiver(User receiver);
    @Query("SELECT m FROM Message m WHERE (m.sender = :sender1 AND m.receiver = :receiver1) OR (m.sender = :sender2 AND m.receiver = :receiver2)")
    List<Message> findBySenderAndReceiverOrReceiverAndSender(User sender1, User receiver1, User sender2, User receiver2);

}