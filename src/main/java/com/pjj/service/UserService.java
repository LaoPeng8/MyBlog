package com.pjj.service;

import com.pjj.entity.User;

public interface UserService {

    User checkUser(String username,String password);

    User findUserByName(String username);

}
