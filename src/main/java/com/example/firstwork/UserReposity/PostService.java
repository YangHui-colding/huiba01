package com.example.firstwork.UserReposity;

import com.example.firstwork.entity.Post;
import com.example.firstwork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public  class PostService {
  @Autowired
  PostReposity postReposity;
  @Autowired
  CategoryService categoryService;
  @Autowired
  CategoryReposity categoryReposity;
    public List<Post>  getHotPosts(Integer number) {
        // 按点赞数，取前limit条
        return postReposity.findFirstByOrderByLikeCountDesc(5);
    }
    public List<Post> getNewPosts(Integer number) {
        // 按创建时间，取前limit条
        return postReposity.findTopByOrderByCreatedAtDesc(5);
    }
    public Post getDetailsPost(Long id) {
        Post post=postReposity.findById(id).orElse(null);//findById从数据库中拿出来的是optional盒子，要用get方法或者orElse方法来拿取其中的。
        if(post!=null){
            post.setViewCount(post.getViewCount()+1);//VIEW+1
            postReposity.save(post);
            return post;
        }else {
            return null;
        }
    }
    public Post createPost(User user, String content,String title,Long categoryId) {
        Post post=new Post();
        post.setUser(user);
        post.setCreatedAt();
        post.setCategory(categoryReposity.findById(categoryId).orElse(null));
        post.setViewCount(0);
        post.setTitle(title);
        post.setContent(content);
        post.setCommentCount(0);
        postReposity.save(post);
        return post;
    }
    public Post updatePost(Post post) {
        postReposity.save(post);
        return post;
    }
    public void deletePost(Long id) {
        postReposity.deleteById(id);
    }
    public Post likePost(Post post) {
        postReposity.save(post);
        return post;
    }
    public Post commentPost(Post post) {
        post.setCommentCount(post.getCommentCount()+1);
        postReposity.save(post);
        return post;
    }

}
