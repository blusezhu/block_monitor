package com.itheima.controller;

import com.itheima.data.pojo.UserEntity;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findAll")
    public List<UserEntity> findAll(){
        return userService.list();
    }

    @RequestMapping(value = "/findById/{id}")
    public UserEntity findById(@PathVariable Integer id){
        return userService.getById(id);
    }

    @RequestMapping(value = "/update")
    public void update(@RequestBody UserEntity user){
        userService.updateById(user);
    }
}
