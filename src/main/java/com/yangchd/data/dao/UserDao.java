package com.yangchd.data.dao;

import com.yangchd.data.table.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author yangchd  2017/12/22.
 */
public interface UserDao extends JpaRepository<User,Serializable> {

    /**
     * 根据用户编号获取用户
     */
    User findByCode(String code);

    /**
     * 根据用户名获取用户
     */
    User findByName(String name);
}
