package com.yangchd.data.service.user;

import com.yangchd.data.table.User;

import java.util.Map;

/**
 * @author yangchd  2017/12/22.
 */
public interface IUserService {

    Map<String, Object> checkLogin(User user);
}
