package com.example.firstwork.controller;

import com.example.firstwork.UserReposity.CategoryReposity;
import com.example.firstwork.UserReposity.CategoryService;
import com.example.firstwork.UserReposity.NotificationReposity;
import com.example.firstwork.entity.Category;
import com.example.firstwork.entity.Post;
import com.example.firstwork.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryReposity categoryReposity;
    @Autowired
    private NotificationReposity notificationReposity;
    @GetMapping("/categorydetail/{id}")
    public String viewcategory(Model model, @PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpSession session) {
        Category category = categoryReposity.findById(id).orElse(null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = categoryService.getCategoryPost(id, pageable);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
            model.addAttribute("unreadCount", unreadCount);
        }
        if (category == null) {
            model.addAttribute("error", "该栏目已经不存在");
            return "redirect:/FirstPage";//后期加上无栏目
        } else {
            model.addAttribute("category", category);
            model.addAttribute("posts", postPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", postPage.getTotalPages());
            model.addAttribute("size", size);
        }
        return "categorydetail";
    }
    @GetMapping("/category")
    public String showCreateForm(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            long unreadCount = notificationReposity.countByUserIdAndReadreadFalse(loginUser.getId());
            model.addAttribute("unreadCount", unreadCount);
        }
        return "category";
    }
    @PostMapping("/category")
    public String createCategory(@RequestParam String name,
                                 @RequestParam String description,
                                 Model model, HttpSession  session) {
       User user = (User) session.getAttribute("loginUser");
       if (user == null) {
           model.addAttribute("error", "You are not logged in");
           return "redirect:/login";
       }
     Integer a=categoryService.setCategory(description,name);
       if (a==1) {
           model.addAttribute("success", "栏目创建成功！");
       }else if (a==2) {
           model.addAttribute("error","您的栏目名已存在");
       }
        return "category";
    }
}