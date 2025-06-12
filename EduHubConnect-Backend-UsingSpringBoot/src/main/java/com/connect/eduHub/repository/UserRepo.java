package com.connect.eduHub.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.connect.eduHub.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
		
	Optional<User> findByEmail(String email);
	@Query("SELECT u FROM User u JOIN u.friends f WHERE f.id = :userId")
	List<User> findFriendsByUserId(Long userId);
	
	
	

}
