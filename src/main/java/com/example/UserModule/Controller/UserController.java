package com.example.UserModule.Controller;

import com.example.UserModule.Model.HpnsConfig;
import com.example.UserModule.Model.User;
import com.example.UserModule.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public List<User> getAllUser()
    {
        return userService.getAllUser();
    }

    @PostMapping("/create")
    public User addUser(@RequestBody User user) throws Exception {
        System.out.println("USER "+user);
        return userService.addUser(user);
    }

    @GetMapping("/json")
    public void insertJsonData() throws IOException {
        userService.insertJsonData();
    }

    @GetMapping("/getJsonData")
    public List<HpnsConfig> readJsonData() throws JsonProcessingException {
        return userService.readJsonData();
    }
}
