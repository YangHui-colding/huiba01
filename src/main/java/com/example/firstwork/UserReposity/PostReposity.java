package com.example.firstwork.UserReposity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.firstwork.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostReposity extends JpaRepository<Post,Long> {
    Post findBy(String title);
   List<Post> findByUserIdOrderByCreatedAtDesc(Long id);
    Page<Post> findByCategoryIdOrderByCreatedAtDesc(Long categoryid, Pageable pageable);
    List<Post> findFirstByOrderByLikeCountDesc(Integer number);
    List<Post> findTopByOrderByCreatedAtDesc(Integer number);
}