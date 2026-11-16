package com.example.firstwork.entity;
import com.example.firstwork.UserReposity.UserReposity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="user")//表名
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    @Column(unique=true,nullable=true)
    private String username;
    @Column(nullable = true)
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Like> likes = new ArrayList<>();
    @Column
    private String avatarUrl;// 头像URL
    @Column(nullable = false)
    private String role = "user";  // user或者admin
    @Column
    private String email;//目前未补齐邮箱验证
    @OneToMany(mappedBy = "user")//注意mappedBy是变量名，不是表名
    private List<Post> posts= new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Comment> comments= new ArrayList<>();
    @Column(name="createAt")
    private LocalDateTime createAt;
    @Column(nullable = false)
    private Integer status = 1;//帖子可看状态，1为可看，0为不可看
    public User() {}
    public User(String name,String password){
        this.username = name;
        this.password = password;
        this.createAt = LocalDateTime.now();
    }
    public User(String username,String password,String email){}
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreatedAt() { this.createAt =LocalDateTime.now(); }
}


