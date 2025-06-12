package com.connect.eduHub.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.User;

public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findByUser(User user);
	List<Post> findByUserIn(Set<User> users);
	@Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findPostsByUserId(Long userId);
	@Query("SELECT p FROM Post p WHERE p.user IN (SELECT f FROM User u JOIN u.friends f WHERE u.id = :userId)")
	List<Post> findFeedPostsByUserId(Long userId);
	
	@Query("SELECT p FROM Post p WHERE p.user.id != :userId ORDER BY function('RAND')")
	List<Post> findAllRandomPostsExcludingUser(@Param("userId") Long userId);
}
