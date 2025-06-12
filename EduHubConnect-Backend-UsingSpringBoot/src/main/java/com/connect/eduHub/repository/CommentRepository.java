package com.connect.eduHub.repository;

import com.connect.eduHub.model.Comment;
import com.connect.eduHub.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> findByPostId(Long postId);
}