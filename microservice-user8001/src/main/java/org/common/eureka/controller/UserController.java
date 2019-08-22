package org.common.eureka.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.common.eureka.entity.SecUser;
import org.common.eureka.service.ISecUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ISecUserService userService;

    /**
     * @param id
     * @return
     */
    @GetMapping("/json/{id}")
    public String queryJsonById(@PathVariable String id) {
        return JSONObject.toJSONString(this.userService.queryByPrimaryKey(id));
    }

    /**
     * 获取用户对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public SecUser queryOne(@PathVariable String id) {
        System.out.println("microservice-user8001服务");
        return this.userService.queryByPrimaryKey(id);
    }

    @GetMapping("/all")
    public String queryAll() {
        List<SecUser> users = this.userService.queryAll(null);
        return JSONArray.toJSONString(users);
    }

    @GetMapping("/name/{userName}")
    public String queryJsonByName(@PathVariable String userName) {
        return JSONObject.toJSONString(this.userService.queryByUsername(userName));
    }
}
