package com.connect.eduHub.services;

import com.connect.eduHub.dto.FriendRequestDTO;
import com.connect.eduHub.mappers.FriendRequestMapper;
import com.connect.eduHub.model.FriendRequest;
import com.connect.eduHub.model.Friendship;
import com.connect.eduHub.model.RequestStatus;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.FriendRequestRepository;
import com.connect.eduHub.repository.FriendShipRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {

    @Autowired private FriendRequestRepository friendRequestRepository;
    @Autowired private UserRepo userRepository;
    @Autowired private FriendRequestMapper friendRequestMapper;
    @Autowired
    private FriendShipRepository friendShipRepository;


    // 1. Send Friend Request
    public FriendRequestDTO sendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("You cannot send a request to yourself");
        }

        // Check if there's already a pending or accepted request
        Optional<FriendRequest> existingRequest = friendRequestRepository
            .findBySenderIdAndReceiverIdAndStatus(senderId, receiverId, RequestStatus.PENDING);

        if (existingRequest.isPresent()) {
            throw new IllegalStateException("Friend request already sent and pending.");
        }

        // Optional: also check for reverse direction if needed
        Optional<FriendRequest> reverseRequest = friendRequestRepository
            .findBySenderIdAndReceiverIdAndStatus(receiverId, senderId, RequestStatus.PENDING);
        
        if (reverseRequest.isPresent()) {
            throw new IllegalStateException("This user has already sent you a request.");
        }

        
        User sender = userRepository.findById(senderId).get();
        User receiver = userRepository.findById(receiverId).get();

        // Save new friend request
        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setSentAt(LocalDateTime.now());
    

        friendRequestRepository.save(request);

        return friendRequestMapper.toDTO(request);
    }


    // 2. Accept or Reject Friend Request
    public FriendRequestDTO respondRequest(Long requestId, RequestStatus status) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);

        // If accepted, add to friends list
        if (status == RequestStatus.ACCEPTED) {
            User sender = request.getSender();
            User receiver = request.getReceiver();

            sender.getFriends().add(receiver);
            receiver.getFriends().add(sender);

            userRepository.save(sender);
            userRepository.save(receiver);
            
            
            Friendship friendship1 = new Friendship();
            friendship1.setUser(sender);
            friendship1.setFriend(receiver);
            friendship1.setStatus("accepted");

            Friendship friendship2 = new Friendship();
            friendship2.setUser(receiver);
            friendship2.setFriend(sender);
            friendship2.setStatus("accepted");

            friendShipRepository.save(friendship1);
            friendShipRepository.save(friendship2);
        }

        return friendRequestMapper.toDTO(friendRequestRepository.save(request));
    }

    // 3. Get all received friend requests for a user
    public List<FriendRequestDTO> getFriendRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<FriendRequest> requests = friendRequestRepository.findByReceiverAndStatus(user, RequestStatus.PENDING);

        return requests.stream()
                .map(friendRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    public List<FriendRequestDTO> getPendingRequests(Long userId) {
        User receiver = userRepository.findById(userId).orElseThrow();
        return friendRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING)
                                      .stream()
                                      .map(friendRequestMapper::toDTO)
                                      .collect(Collectors.toList());
    }
}
