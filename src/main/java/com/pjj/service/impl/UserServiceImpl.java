package com.pjj.service.impl;

import com.pjj.entity.User;
import com.pjj.mapper.UserMapper;
import com.pjj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {//e10adc3949ba59abbe56e057f20f883e
        User user = userMapper.findUserByUsernameAndPassword(username,password);
        return user;
    }

    @Override
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }


}
