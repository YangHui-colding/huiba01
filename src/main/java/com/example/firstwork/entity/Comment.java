package com.example.firstwork.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment-id")
    private Long id;
    @Column
    private String content;
    @Column
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="User_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replies;
    @Column
    private Integer likeCount=0;//这中包装类对象值最好初始时设定值，防止后续使用的时候null错误
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @Column(nullable = true)
    private Integer status = 0;//评论可看状态，0为可看，1为不可看
    public Post getPost() {
        return post;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(Integer likes) {this.likeCount = likes;}
    public String getContent() {
        return content;
    }
    public void setContent(String comtent) {this.content = comtent;}
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt() {this.createdAt =LocalDateTime.now();}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {this.user = user;}
    public Long getId() { return id; }
    public void setPost(Post post) {this.post = post;}
    public List<Comment> getReplies() {
        return replies;
    }
    public void setParentComment(Comment parentComment) {this.parentComment =parentComment;}//设置为顶级评论
    public void setReplies(List<Comment> replies) {this.replies = replies;}
}
