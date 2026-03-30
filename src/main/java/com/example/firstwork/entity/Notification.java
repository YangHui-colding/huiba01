package com.example.firstwork.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;  // 接收通知的用户ID
    @Column(nullable = false)
    private String content;  // 通知内容，如："张三赞了你的帖子"
    @Column(nullable = false)
    private String link;  // 点击跳转链接
    @Column(name="readread",nullable = false)
    private boolean readread = false;  // 是否已读，默认 false
    @Column(nullable = false)
    private LocalDateTime createdAt;  // 创建时间
    public Notification() {}
    public Notification(Long userId, String content, String link) {
        this.userId = userId;
        this.content = content;
        this.link = link;
        this.createdAt = LocalDateTime.now();
        this.readread = false;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public boolean isReadread() { return readread; }
    public void setReadread(boolean read) { this.readread = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}