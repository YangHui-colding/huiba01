package com.example.firstwork.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="likelike")
public class Like {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
    private LocalDateTime createdAt;
    public Like(User user, Post post, LocalDateTime createdAt) {}
    public Like(){}
    public Like(User user, Post post) {this.user=user;this.post=post;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt() {this.createdAt = LocalDateTime.now();}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public Post getPost() {return post;}
    public void setPost(Post post) {this.post = post;}
}
