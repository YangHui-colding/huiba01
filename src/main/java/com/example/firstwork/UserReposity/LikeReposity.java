package com.example.firstwork.UserReposity;
import com.example.firstwork.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LikeReposity extends JpaRepository<Like,Long> {
    Like findByUserIdAndPostId(Long userId, Long postId);
    long countByPostId(Long postId);
    @Modifying
    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
