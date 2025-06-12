package com.connect.eduHub.services;

import com.connect.eduHub.dto.UserDTO;
import com.connect.eduHub.mappers.UserMapper;
import com.connect.eduHub.model.Friendship;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.FriendRequestRepository;
import com.connect.eduHub.repository.FriendShipRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired private UserRepo userRepository;
    @Autowired private UserMapper userMapper;
    @Autowired private FriendRequestRepository friendRequestRepository;
    @Autowired private FriendShipRepository friendShipRepository;

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO).orElse(null);
    }

    public UserDTO updateUserProfile(Long id, UserDTO updatedUser) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(updatedUser.getName());
        user.setBio(updatedUser.getBio());
        user.setProfileImage(updatedUser.getProfileImage());
        return userMapper.toDTO(userRepository.save(user));
    }
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<UserDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFriends()
                   .stream()
                   .map(userMapper::toDTO)
                   .collect(Collectors.toList());
    }
    
    
    public Set<Long> getFriendsOfUser(Long userId) {
        // Fetch all friendships where the user is either 'user' or 'friend' and the status is 'accepted'
        List<Friendship> friendships = friendShipRepository.findByUserIdOrFriendIdAndStatus(userId, "accepted");

        // Convert the list of friendships into a set of friends' IDs
        Set<Long> friends = new HashSet<>();
        for (Friendship friendship : friendships) {
            if (friendship.getUser().getId().equals(userId)) {
                friends.add(friendship.getFriend().getId());
            } else {
                friends.add(friendship.getUser().getId());
            }
        }
        return friends;
    }

    
      public List<UserDTO> getAvailableUsers(Long userId) {
        List<User> allUsers = userRepository.findAll();
        List<User> friendsAndPending = friendRequestRepository.findFriendsAndPending(userId);

        return allUsers.stream()
            .filter(user -> !user.getId().equals(userId)) // exclude self
            .filter(user -> !friendsAndPending.contains(user)) // exclude friends and pending
            .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(),user.getBio(), user.getProfileImage()))
            .collect(Collectors.toList());
    }
    
    
    
      public List<UserDTO> getAllUsersExcludingSelf(Long userId) {
    	    List<User> userList = userRepository.findAll();

    	    return userList.stream()
    	        .filter(user -> !user.getId().equals(userId)) // Exclude the logged-in user
    	        .map(userMapper::toDTO)
    	        .collect(Collectors.toList());
    	}
   
    
    
}
