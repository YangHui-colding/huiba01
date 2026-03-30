package com.example.firstwork.controller;
import com.example.firstwork.UserReposity.CategoryReposity;
import com.example.firstwork.UserReposity.NotificationReposity;
import com.example.firstwork.entity.Category;
import com.example.firstwork.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.firstwork.entity.Post;
import java.util.List;
import com.example.firstwork.UserReposity.PostService;

@Controller
public class FirstPageController {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryReposity categoryReposity;
    @Autowired
    private NotificationReposity notificationReposity;
        @GetMapping("/FirstPage")
    public String firstPage(Model model, HttpSession session) {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser != null) {
                long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
                model.addAttribute("unreadCount", unreadCount);

            }
            List<Post> hotPosts = postService.getHotPosts(5);  // 取前5
            // 获取最新帖子
            List<Post> newPosts = postService.getNewPosts(5);
            // 获取所有栏目
            List<Category> allcategory=categoryReposity.findAll();
            model.addAttribute("hotPosts", hotPosts);
            model.addAttribute("newPosts", newPosts);
            model.addAttribute("categories", allcategory);
            return "FirstPage";
    }
        @PostMapping("/FirstPage")
    public String getcolunm(Model model) {
            return "FirstPage";
    }

}
