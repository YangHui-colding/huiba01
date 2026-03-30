package com.example.firstwork.entity;
import com.example.firstwork.util.MarkdownUtil;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Post")
public class Post {
    @Id
@GeneratedValue
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")//定义content在数据库中为TEXT文本
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 作者
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;  // 所属栏目
    @OneToMany(mappedBy = "post")
    private List<Like> likes;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments=new ArrayList<>();
    @Column(nullable = true)
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private Integer viewCount = 0;  // 浏览量
    @Column(nullable = true)
    private Integer commentCount = 0;
    @Column(nullable = true)
    private Integer status = 1;//帖子可看状态，1为可看，0为不可看
    @Column(nullable = true)
    private Integer likeCount = 0;
    public User getUser() {return user;}
    public Category getCategory() {return this.category;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public Integer getViewCount() {return viewCount;}
    public long getId() {return id;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public Integer getCommentCount() {return commentCount;}
    public List<Comment> getComments() {return comments;}
    public void setViewCount(Integer viewCount) {this.viewCount = viewCount;}
    public void setUser(User user) {this.user = user;}
    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
       public void setCategory(Category category) {this.category= category;}//后期要加设置栏目的判断和失败
    public void setCreatedAt() {this.createdAt =LocalDateTime.now();}
    public void setCommentCount(Integer count) {this.commentCount= count;}
    public void setLikeCount(Integer likes) {this.likeCount= likes;}
    public Integer getLikeCount() {return likeCount;}@Transient
    private static MarkdownUtil markdownUtil;
    public void setMarkdownUtil(MarkdownUtil markdownUtil) {
        this.markdownUtil = markdownUtil;
    }
    @Transient
    public String getContentHtml() {
        if (markdownUtil == null) {
            return content;  // 降级返回原文
        }
        return markdownUtil.renderToHtml(this.content);
    }
}
