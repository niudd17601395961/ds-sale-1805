package com.mr.service;

import com.mr.model.TMallUserAccount;

public interface UserService {
    TMallUserAccount selectLogin(String userName, String password);

    TMallUserAccount selectUserByName(String userName);

    void saveUser(TMallUserAccount user);
}
