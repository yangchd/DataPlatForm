package com.yangchd.data.controller;

import com.yangchd.data.service.user.IUserService;
import com.yangchd.data.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangchd  2017/12/22.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private final IUserService userService;

    @Autowired
    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user")
    public Map<String, Object> getLogin(@RequestBody User user) {
        Map<String, Object> rMap = new HashMap<>(4);
        try {
            rMap = userService.checkLogin(user);
        } catch (Exception e) {
            rMap.put("code", "1");
            rMap.put("msg", e.getMessage());
        }
        return rMap;
    }

}
