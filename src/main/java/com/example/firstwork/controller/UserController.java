package com.example.firstwork.controller;
import com.example.firstwork.UserReposity.*;
import com.example.firstwork.entity.Notification;
import com.example.firstwork.entity.Post;
import com.example.firstwork.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired//用户数据转化为SQL语言
    private UserReposity userReposity;
    @Autowired
    private PostReposity postReposity;
    @Autowired
    private CommentReposity commentReposity;
    @Autowired
    private NotificationReposity notificationReposity;
    @Autowired
    private CategoryReposity categoryReposity;
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
    @PostMapping("/register")
    public String register (User user, Model model) {
        UserService registerationGudger = new UserService();
            if (userReposity.findByUsername(user.getUsername())!=null){
                model.addAttribute("error", "用户名已存在，请换一个");
                return "register";
            }else if(registerationGudger.passwordGudge(user.getPassword())){
                model.addAttribute("success","恭喜你成为辉吧的一员");
                userReposity.save(user);//保存user的信息到reposity里面
                return "register";
            }else model.addAttribute("error","密码不符合要求，还想加入辉吧？");
            return "register";
    }
    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(User user, Model model, HttpSession session) {
        if ((userReposity.findByUsername(user.getUsername())!=null)&&(user.getPassword().equals((userReposity.findByUsername(user.getUsername())).getPassword()))) {
            User dbUser = userReposity.findByUsername(user.getUsername());
            session.setAttribute("loginUser", dbUser);
            return "redirect:/FirstPage";  // 跳转到首页
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @PostMapping("/user/avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) throws IOException {
            User user=(User)session.getAttribute("loginUser");
            String fileName = file.getOriginalFilename();
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/avatars/";
            File dest = new File(uploadDir + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            String avatarUrl = "/avatars/" + fileName;
            user.setAvatarUrl(avatarUrl);
            userReposity.save(user);
            session.setAttribute("loginUser", user);
            redirectAttributes.addFlashAttribute("success", "头像更新成功！");
        return "redirect:/user/"+user.getId();
    }
    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable Long id, Model model, HttpSession session) {
        User user =userReposity.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/FirstPage";
        }
        List<Post> userPosts = postReposity.findByUserIdOrderByCreatedAtDesc(id);
        User loginUser = (User) session.getAttribute("loginUser");
        long unreadCount = 0;
        notificationReposity.markAllAsRead(id);
        List<Notification> notices = notificationReposity.findByUserIdOrderByCreatedAtDesc(id);
        model.addAttribute("user", user);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("notices", notices);
        return "user_profile";
    }
        @GetMapping("/admin")
        public String adminPanel(Model model, HttpSession session) {
        User user=(User)session.getAttribute("loginUser");
        if(user.getRole().equals("admin")){
            model.addAttribute("users", userReposity.findAll());
            model.addAttribute("posts", postReposity.findAll());
            model.addAttribute("comments", commentReposity.findAll());
            model.addAttribute("category", categoryReposity.findAll());
            return "admin";}else {
            model.addAttribute("error", "你™不是管理员");
            return "admin";
        }
        }
        @PostMapping("/admin/user/delete/{id}")
        public String deleteUser(@PathVariable Long id,Model model, HttpSession session) {
            User user=(User)session.getAttribute("loginUser");
            if(user.getRole().equals("admin")){
            if(userReposity.findById(id).equals("admin")){
                model.addAttribute("error", "它是管理员,你删不了!");
                return "admin";
            }
            userReposity.deleteById(id);
            return "redirect:/admin";
            }else {
                model.addAttribute("error", "你™不是管理员");
                return "admin";
            }
        }
        @PostMapping("/admin/post/delete/{id}")
        public String deletePost(@PathVariable Long id,Model model, HttpSession session) {
            User user=(User)session.getAttribute("loginUser");
            if(user.getRole().equals("admin")) {
                postReposity.deleteById(id);
                return "redirect:/admin";
            }else {
                model.addAttribute("error", "你™不是管理员");
                return "admin";
            }
        }
        @PostMapping("/admin/comment/delete/{id}")
        public String deleteComment(@PathVariable Long id,Model model, HttpSession session) {
            User user=(User)session.getAttribute("loginUser");
            if(user.getRole().equals("admin")) {
                commentReposity.deleteById(id);
                return "redirect:/admin";
            }else {
                model.addAttribute("error", "你™不是管理员");
                return "admin";
            }
        }
    @PostMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id,Model model, HttpSession session) {
        User user=(User)session.getAttribute("loginUser");
        if(user.getRole().equals("admin")) {
            categoryReposity.deleteById(id);
            return "redirect:/admin";
        }else {
            model.addAttribute("error", "你™不是管理员");
            return "admin";
        }
    }
}