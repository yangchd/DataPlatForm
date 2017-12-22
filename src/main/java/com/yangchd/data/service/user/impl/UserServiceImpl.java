package com.yangchd.data.service.user.impl;

import com.yangchd.data.dao.UserDao;
import com.yangchd.data.service.user.IUserService;
import com.yangchd.data.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangchd  2017/12/22.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Map<String, Object> checkLogin(User user) {
        Map<String, Object> rMap = new HashMap<>(4);
        User userLogin = userDao.findByCode(user.getCode());
        if (null != userLogin && !"".equals(userLogin.getCode())) {
            if (null != user.getPassword() && user.getPassword().equals(userLogin.getPassword())) {
                rMap.put("code","0");
                rMap.put("msg","登录成功！");
            } else {
                rMap.put("code","1");
                rMap.put("msg","用户名或密码错误！");
            }
        } else {
            rMap.put("code","1");
            rMap.put("msg","用户名不存在！");
        }
        return rMap;
    }
}
