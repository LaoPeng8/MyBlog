package com.pjj.mapper;

import com.pjj.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User findUserByName(String username);

    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
