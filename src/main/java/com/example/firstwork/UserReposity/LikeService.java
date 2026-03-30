package com.example.firstwork.UserReposity;
import com.example.firstwork.entity.Like;
import com.example.firstwork.entity.Notification;
import com.example.firstwork.entity.Post;
import com.example.firstwork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LikeService {
    @Autowired
    private LikeReposity likeReposity;
    @Autowired
    private PostReposity postReposity;
    @Autowired
    private NotificationReposity notificationReposity;
    @Transactional
    public boolean toLike(Long postId, User loginUser) {
        Post post = postReposity.findById(postId).orElseThrow();
        if ((likeReposity.findByUserIdAndPostId(loginUser.getId(), postId))!=null) {
            likeReposity.deleteByUserIdAndPostId(loginUser.getId(), postId);
            post.setLikeCount(post.getLikeCount() - 1);
            postReposity.save(post);
            return false;//取消点赞
        } else {
            Like like = new Like(loginUser, post);
            like.setUser(loginUser);
            like.setPost(post);
            like.setCreatedAt();
            likeReposity.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
            postReposity.save(post);
                Notification notice = new Notification(); // 创建通知（不是给自己的帖子）
                notice.setUserId(post.getUser().getId());
                notice.setContent(loginUser.getUsername() + " 赞了你的帖子");
                notice.setLink("/postdetail/" + postId);
                notice.setCreatedAt(LocalDateTime.now());
                notificationReposity.save(notice);
            return true;  // 点赞成功
        }
    }
    public long getLikeCount(Long postId) {
        return likeReposity.countByPostId(postId);
    }
    public boolean isLiked(Long postId, Long userId) {
        return (likeReposity.findByUserIdAndPostId(userId, postId))!=null;
    }
}
