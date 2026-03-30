package com.example.firstwork.UserReposity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.firstwork.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryReposity extends JpaRepository<Category, Long> {
        Category findBy(String categoryName);
    Category findByName(String categoryName);
    List<Category> findAll();
    Category findById(long id);//注意返回的是optional容器，要用orElse或者get方法
}