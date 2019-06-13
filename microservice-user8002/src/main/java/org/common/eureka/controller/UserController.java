package org.common.eureka.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.common.eureka.dao.IUserDao;
import org.common.eureka.entity.SecUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/user")
public class UserController {

    @Autowired
    private IUserDao userDao;

    /**
     * @param id
     * @return
     */
    @GetMapping("/json/{id}")
    public String queryJsonById(@PathVariable String id) {
        return JSONObject.toJSONString(this.userDao.getOne(id));
    }

    /**
     * 获取用户对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public SecUser queryOne(@PathVariable String id) {
        return this.userDao.getOne(id);
    }

    @GetMapping("/all")
    public String queryAll() {
        List<SecUser> users = this.userDao.findAll();
        return JSONArray.toJSONString(users);
    }

    @GetMapping("/name/{userName}")
    public String queryJsonByName(@PathVariable String userName) {
        return JSONObject.toJSONString(this.userDao.findUserByName(userName));
    }
}
