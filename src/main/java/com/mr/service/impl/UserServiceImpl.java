package com.mr.service.impl;

import com.mr.mapper.UserMapper;
import com.mr.model.TMallUserAccount;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public TMallUserAccount selectLogin(String userName, String password) {
        return userMapper.selectLogin(userName,password);
    }

    @Override
    public TMallUserAccount selectUserByName(String userName) {
        return userMapper.selectUserByName(userName);
    }

    @Override
    public void saveUser(TMallUserAccount user) {
        userMapper.saveUser(user);
    }
}
