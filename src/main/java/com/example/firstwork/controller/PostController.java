package com.example.firstwork.controller;
import com.example.firstwork.UserReposity.*;
import com.example.firstwork.entity.*;
import com.example.firstwork.util.MarkdownUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryReposity categoryReposity;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private PostReposity postReposity;
    @Autowired
    private NotificationReposity notificationReposity;
    @Autowired
    private MarkdownUtil markdownUtil;
    @GetMapping("/postdetail/{id}")
    public String viewpost(Model model,@PathVariable Long id,HttpSession session){
        Post post=postService.getDetailsPost(id);
        post.setMarkdownUtil(markdownUtil);
        User loginUser = (User) session.getAttribute("loginUser");
        Pageable pageable = PageRequest.of(0, 10);
        if (loginUser != null) {
            long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
            model.addAttribute("unreadCount", unreadCount);
        }
        if(post!=null){
            List<Comment> comments = commentService.getCommentsTree(id,pageable);
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            return "postdetail";
        }else {
            return "redirect:/FirstPage";//后期换成帖子被删除或者帖子已不在
        }
    }
    @PostMapping("/post/{postId}/comment")  // 与查看帖子路径一致
    public String addComment(@PathVariable Long postId,@RequestParam(required = false) Long parentId, @RequestParam String content, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");//用redirectattributes，把数据传给下一页，完成评论添加和用户评论显示
        if (loginUser == null) {
            model.addAttribute("error", "请先登录");
            return "redirect:/login";
        }
        if (loginUser != null) {
            long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
            model.addAttribute("unreadCount", unreadCount);
        }
        if (content == null || content.trim().isEmpty()) {
            model.addAttribute("error", "评论内容不能为空");
            return "postdetail";
        }
        Post post=postReposity.findById(postId).orElse(null);
        if(parentId!=null){
            commentService.addReply(content, loginUser, postId, parentId);
        }else {
            commentService.addTopLevelComment(content, loginUser, postId);
        }
        if (!loginUser.getId().equals(post.getUser().getId())) {
            Notification notice = new Notification();
            notice.setUserId(post.getUser().getId());
            notice.setContent(loginUser.getUsername() + " 评论了你的帖子");
            notice.setLink("/postdetail/" + postId);
            notice.setCreatedAt(LocalDateTime.now());
            notice.setReadread(false);
            notificationReposity.save(notice);
        }
        model.addAttribute("success", "评论成功！");
        return "redirect:/postdetail/"+postId;
    }
    @GetMapping("/post")
    public String createpost(Model model, HttpSession session) {
        // 检查登录
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
            model.addAttribute("unreadCount", unreadCount);
        }
        List<Category> categories = categoryReposity.findAll();
        model.addAttribute("categories", categories);
        return "post";
    }
    @PostMapping("/post")
    public String createpost(Model model, HttpSession httpSession, @RequestParam String title,@RequestParam Long categoryId,@RequestParam String content){
        if(title==null||categoryId==null||content==null){
            model.addAttribute("error"+"标题和栏目和内容不能为空！");//后期加入错误信息页面提示
            return "post";
        }else {
            User temuser=(User)httpSession.getAttribute("loginUser");
            Post tempost=postService.createPost(temuser,content,title,categoryId);
            model.addAttribute("success","创建成功！");
            return "redirect:/postdetail/" + tempost.getId();
        }
    }
    @PostMapping("/post/{postId}/like")
    @ResponseBody//点赞实现
    public Map<String, Object> likePost(@PathVariable Long postId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser=(User)session.getAttribute("loginUser");
        if (loginUser==null) {
            result.put("success",false);
            result.put("message","请先登录");
            return result;
        }
        boolean liked=likeService.toLike(postId, loginUser);
        Post post=postReposity.findById(postId).orElse(null);
        if (!loginUser.getId().equals(post.getUser().getId())) {
            Notification notice = new Notification();
            notice.setUserId(post.getUser().getId());
            notice.setContent(loginUser.getUsername() + " 赞了你的帖子");
            notice.setLink("/postdetail/" + postId);
            notice.setCreatedAt(LocalDateTime.now());
            notice.setReadread(false);
            notificationReposity.save(notice);
        }
        result.put("success",true);
        result.put("liked",liked);
        result.put("likeCount", post.getLikeCount());
        return result;
    }
}
