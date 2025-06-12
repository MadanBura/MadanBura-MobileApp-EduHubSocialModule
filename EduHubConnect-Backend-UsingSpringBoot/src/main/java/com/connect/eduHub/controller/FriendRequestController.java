package com.connect.eduHub.controller;

import com.connect.eduHub.dto.FriendRequestDTO;
import com.connect.eduHub.dto.UserDTO;
import com.connect.eduHub.model.RequestStatus;
import com.connect.eduHub.services.FriendRequestService;
import com.connect.eduHub.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    
    @Autowired UserService userService;

    // 1. Send Friend Request
    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            friendRequestService.sendRequest(senderId, receiverId);
            return ResponseEntity.ok("Friend request sent successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong.");
        }
    }

    // 2. Respond to Friend Request (Accept/Reject)
    @PutMapping("/respond")
    public FriendRequestDTO respondToRequest(
            @RequestParam Long requestId,
            @RequestParam RequestStatus status) {
        return friendRequestService.respondRequest(requestId, status);
    }

    // 3. View received friend requests
    @GetMapping("/requests/{userId}")
    public List<FriendRequestDTO> getFriendRequests(@PathVariable Long userId) {
        return friendRequestService.getFriendRequests(userId);
    }
    
    //4. see all users who are available on this app
    @GetMapping("/available/{userId}")
    public List<UserDTO> getAvailableUsers(@PathVariable Long userId) {
        return userService.getAvailableUsers(userId);
    }
    
}
