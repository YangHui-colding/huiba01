package com.example.firstwork.UserReposity;
import com.example.firstwork.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public interface NotificationReposity extends JpaRepository<Notification, Long> {
    long countByUserIdAndReadreadFalse(Long userId);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.readread = true WHERE n.userId = :userId")
    void markAllAsRead(@Param("userId") Long userId);
    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}