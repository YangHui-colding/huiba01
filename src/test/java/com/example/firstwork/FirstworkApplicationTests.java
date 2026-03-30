package com.example.firstwork;

import com.example.firstwork.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirstworkApplicationTests {

    @Test
    void contextLoads() {
        User zhangsan = new User("zhangsan", "123");
        User zhaolin = new User("zhaolin", "123");
        System.out.println(zhangsan.getPassword());
        System.out.println(zhaolin.getPassword());
    }

}
