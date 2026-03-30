package com.example.firstwork.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="Category")
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long id;
    @OneToMany(mappedBy = "category")
    private List<Post> posts= new ArrayList<>();
    @Column(name = "name")
    private String name;
    @Column(name="description")
    private String description;
    @Column
    private Integer viewCount=0;
    private void setCategory(String categoryDescription){
        this.description = categoryDescription;
    }
    public Category(String categoryName,String categoryDescription){
        this.name = categoryName;
        this.description = categoryDescription;
    }
    public Category(){}
    public void setName(String categoryName){
        this.name = categoryName;
    }
    public void setDescription(String categoryDescription){
        this.description = categoryDescription;
    }
    public String getName(String CategoryName){
        return this.name =CategoryName;
    }
    public String getDescription(String CategoryDescription){
        return this.description =CategoryDescription;
    }
    public Integer getViewCount(){return this.viewCount;}
    public void setViewCount(Integer viewCount){this.viewCount = viewCount;}
    public Long getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
}
