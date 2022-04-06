package com.march.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.march.common.dto.LoginDto;
import com.march.common.lang.Result;
import com.march.entity.User;
import com.march.service.UserService;
import com.march.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        User user = userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        Assert.notNull(user,"用户不存在");

        if(!SecureUtil.md5(user.getPassword()).equals(SecureUtil.md5(loginDto.getPassword()))){
//            UPDATE m_user set PASSWORD=MD5(PASSWORD)
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());


        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");
        return Result.succ(MapUtil.builder()
        .put("id",user.getId())
        .put("username",user.getUsername())
        .put("avatar",user.getAvatar())
        .put("email",user.getEmail())
        .map()
        );
    }

    @RequiresAuthentication
    @RequestMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.succ("注销成功！");
    }
}
