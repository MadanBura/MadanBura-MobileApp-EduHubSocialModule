package com.connect.eduHub.repository;

import com.connect.eduHub.model.FriendRequest;
import com.connect.eduHub.model.User;
import com.connect.eduHub.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, RequestStatus status);
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = 'PENDING'")
    List<FriendRequest> findPendingRequestsByUserId(Long userId);
    
    
    @Query("SELECT fr.receiver FROM FriendRequest fr WHERE fr.sender.id = :userId AND fr.status IN ('PENDING', 'ACCEPTED') " +
    	       "UNION " +
    	       "SELECT fr.sender FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status IN ('PENDING', 'ACCEPTED')")
    	List<User> findFriendsAndPending(@Param("userId") Long userId);
    
    
    
    Optional<FriendRequest> findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, RequestStatus status);
    Optional<FriendRequest> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdAndStatus(
    	    Long senderId1, Long receiverId1, Long senderId2, Long receiverId2, RequestStatus status);

}