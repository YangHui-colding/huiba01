package com.example.firstwork.UserReposity;

import com.example.firstwork.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReposity extends JpaRepository<Comment, Long> {
    List<Comment> findFirstByOrderByLikeCountDesc(Integer number);
    List<Comment> getCommentsByPostId(Long id);
   Optional<Comment> findById(Long id);
    Page<Comment> findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(Long postId, Pageable pageable);
    List<Comment> findByParentCommentOrderByCreatedAtAsc(Comment comment);
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);
}
