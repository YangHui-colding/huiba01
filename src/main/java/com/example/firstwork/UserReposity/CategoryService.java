package com.example.firstwork.UserReposity;
import com.example.firstwork.entity.Category;
import com.example.firstwork.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryReposity categoryReposity;
    @Autowired
    private PostReposity postReposity;
    public Category getDetailsCategory(Long id) {//看栏目
        Category category = categoryReposity.findById(id).orElse(null);
        if(category == null){
            return null;
        }else {
           category.setViewCount(category.getViewCount()+1);
            return category;
        }
       }
    public Integer setCategory(String CategoryDescription,String CategoryName) {//设置栏目
       if( categoryReposity.findByName(CategoryName)!=null){
         return 0; //后期要加创建栏目失败
       }
        Category category=new Category();
        category.setDescription(CategoryDescription);
        category.setName(CategoryName);
        category.setViewCount(category.getViewCount()+1);
        categoryReposity.save(category);
        return 1;
    }
  public Page<Post> getCategoryPost(Long categoryid,Pageable pageable) {
     return postReposity.findByCategoryIdOrderByCreatedAtDesc(categoryid,pageable);
  }
}
