package com.example.firstwork.UserReposity;

import com.example.firstwork.entity.Comment;
import com.example.firstwork.entity.Notification;
import com.example.firstwork.entity.Post;
import com.example.firstwork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentReposity commentReposity;
    @Autowired
    private PostReposity postReposity;
    @Autowired
    private NotificationReposity notificationReposity;
    public List<Comment> getHotComments() {
        return commentReposity.findFirstByOrderByLikeCountDesc(5);
    }
    public List<Comment> getCommentsByPostId(Long postid) {
        return commentReposity.getCommentsByPostId(postid);
    }
    public Comment addTopLevelComment(String content, User user, Long postCommentId) {
        Post post = postReposity.findById(postCommentId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setParentComment(null);  // 顶级评论
        comment.setCreatedAt();
        comment.setLikeCount(0);
        comment.setStatus(0);
        return commentReposity.save(comment);
    }
    public Comment addReply(String content, User user, Long postId, Long parentId) {
        Post post = postReposity.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        Comment parentComment = (commentReposity.findById(parentId).orElse(null));
        Comment reply = new Comment();
        reply.setContent(content);
        reply.setUser(user);
        reply.setPost(post);
        reply.setParentComment(parentComment);  // 设置父评论ID
        reply.setCreatedAt();
        reply.setLikeCount(0);
        reply.setStatus(0);
        Comment savedReply = commentReposity.save(reply);
        if (!user.getId().equals(parentComment.getUser().getId())) {
            Notification notice = new Notification();
            notice.setUserId(parentComment.getUser().getId());
            notice.setContent(user.getUsername() + " 回复了你的评论");
            notice.setLink("/postdetail/" + postId);
            notice.setCreatedAt(LocalDateTime.now());
            notice.setReadread(false);
            notificationReposity.save(notice);
        }
        return savedReply;
    }
    public List<Comment> getCommentsTree(Long postId,Pageable pageable) {
        List<Comment> topComments = (commentReposity.findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(postId,pageable)).getContent();
        for (Comment top : topComments) {
            List<Comment> replies = commentReposity.findByParentCommentOrderByCreatedAtAsc(top);
            top.setReplies(replies);
        }
        return topComments;
    }
}
