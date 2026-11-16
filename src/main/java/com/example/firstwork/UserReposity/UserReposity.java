package com.example.firstwork.UserReposity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.firstwork.entity.User;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface UserReposity extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean findByPassword(String password);
    List<User> id(long id);
}
