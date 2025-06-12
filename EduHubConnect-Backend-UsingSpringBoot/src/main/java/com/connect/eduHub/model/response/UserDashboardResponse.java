package com.connect.eduHub.model.response;

import java.util.List;

import com.connect.eduHub.dto.FriendRequestDTO;
import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDashboardResponse {
    private UserDTO userProfile;
    private List<UserDTO> friends;
    private List<PostDTO> feedPosts;
    private List<FriendRequestDTO> pendingRequests;
    private int unread_notificationCount;
}