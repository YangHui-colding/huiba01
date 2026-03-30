package com.example.firstwork.UserReposity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserReposity temUserReposity;
    public boolean passwordGudge(String password) {
        if ((password.length() < 6)||(password.length() > 18)) {
            return false;
        } else if (password.matches(".*[0-9]*[a-zA-Z]*")) {
            return true;
        } else {
            return false;
        }
    }
}