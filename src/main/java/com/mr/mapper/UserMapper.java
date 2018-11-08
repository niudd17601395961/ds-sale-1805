package com.mr.mapper;

import com.mr.model.TMallUserAccount;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    TMallUserAccount selectLogin(@Param("userName") String userName,
                                 @Param("password") String password);

    TMallUserAccount selectUserByName(@Param("userName") String userName);

    void saveUser(TMallUserAccount user);
}
