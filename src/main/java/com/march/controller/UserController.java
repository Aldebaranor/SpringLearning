package com.march.controller;


import com.march.common.lang.Result;
import com.march.entity.User;
import com.march.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author song
 * @since 2022-03-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public Object index(){
        User user = userService.getById(1);
        return Result.succ(200,"操作成功",user);
    }

}
