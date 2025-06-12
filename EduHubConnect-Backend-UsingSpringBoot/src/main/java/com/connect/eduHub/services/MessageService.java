package com.connect.eduHub.services;

import com.connect.eduHub.dto.MessageDTO;
import com.connect.eduHub.mappers.MessageMapper;
import com.connect.eduHub.model.Message;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.MessageRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired(required = true)
    private MessageMapper messageMapper;

    public MessageDTO sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());

        return messageMapper.toDTO(messageRepository.save(message));
    }

    public List<MessageDTO> get(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id).orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(user2Id).orElseThrow(() -> new RuntimeException("User2 not found"));

        List<Message> messages = messageRepository.findBySenderAndReceiverOrReceiverAndSender(user1, user2, user1, user2);
        return messages.stream().map(messageMapper::toDTO).collect(Collectors.toList());
    }
}
