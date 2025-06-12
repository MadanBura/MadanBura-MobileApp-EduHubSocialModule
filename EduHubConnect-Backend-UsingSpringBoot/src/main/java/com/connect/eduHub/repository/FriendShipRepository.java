package com.connect.eduHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.connect.eduHub.model.Friendship;

public interface FriendShipRepository extends JpaRepository<Friendship, Long> {

	//List<Friendship> findByUserIdOrFriendIdAndStatus(Long userId1, Long userId2, String status);
	@Query("SELECT f FROM Friendship f WHERE (f.user.id = :userId OR f.friend.id = :userId) AND f.status = :status")
	List<Friendship> findByUserIdOrFriendIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	
}
