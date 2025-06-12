package com.connect.eduHub.repository;

import com.connect.eduHub.model.Like;
import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    Optional<Like> findByUserAndPost(User user, Post post);
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    int countByPostId(Long postId);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    @Query("DELETE FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    void deleteByUserIdAndPostId(Long userId, Long postId);
	
    List<Like> findAllByPost(Post post);
	
	@Query("SELECT l.user FROM Like l WHERE l.post = :post")
	List<User> findUsersWhoLikedPost(@Param("post") Post post);

}