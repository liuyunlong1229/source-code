package org.springboot.integration.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springboot.integration.service.RedisService;
import org.springboot.integration.service.UserService;
import org.springboot.integration.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by thinkpad on 2017/5/13.
 * http://localhost:8080/swagger-ui.html 访问。
 */

@Api(value = "用户操作接口API")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    static Map<Long, UserVO> users = Collections.synchronizedMap(new HashMap<Long, UserVO>());

    @ApiOperation(value = "获取用户列表", notes = "查询所有用户信息")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<UserVO> getUsers() {
      
        List<UserVO>  userList= redisService.getList("userList", UserVO.class);
        if(userList == null){
        	 System.out.println("缓存中没有用户列表，准备查询数据库了。。。。。");
        	 userList=userService.findAll();
             redisService.setList("userList", userList);
        }
       
        return userList;

    }

    @ApiOperation(value = "查询用户详情", notes = "根据url中的用户编码查询", response = UserVO.class)
    @ApiImplicitParam(name = "uid", value = "编码", paramType = "path", required = true)
    @RequestMapping(value = "/user/{uid}", method = RequestMethod.GET)
    public UserVO getUser(@PathVariable("uid") String userid) {
        System.out.println("测试Make的作用1111111");
        return userService.getUserById(userid);

    }

    @ApiOperation(value = "新增用户", notes = "根据用户信息新增用户")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "userName", value = "名称", paramType = "query", required = true),
         @ApiImplicitParam(name = "age", value = "性别", paramType = "query", required = true),
         @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true)
    }
    )
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String addUser(UserVO user) {
        Long uid = userService.addUser(user);
        System.out.println("新增用户的主键为==" + uid);
        redisService.flushDB();
        return "success";

    }


    @ApiOperation(value = "修改用户", notes = "根据用户编码修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "编码", paramType = "query", required = true),
            @ApiImplicitParam(name = "userName", value = "名称", paramType = "query", required = true),
            @ApiImplicitParam(name = "age", value = "性别", paramType = "query", required = true),
            @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true)
    }
    )
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public String updateUser(UserVO user) {
        System.out.println("用户编号||" + user.getUid());
        userService.updateUserByUid(user);
        return "success";

    }


    @ApiOperation(value = "删除用户", notes = "根据用户编码删除用户")
    @ApiImplicitParam(name = "uid", value = "编码", paramType = "path", required = true)
    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    public String delUser(@PathVariable Long uid) {
        userService.deleteUser(uid);
        return "success";

    }


    @ApiOperation(value = "批量增加用户")
    @RequestMapping(value = "/user/batch", method = RequestMethod.POST)
    public String addUserBatch() {
        List<UserVO> userVOList = new ArrayList<UserVO>();
        UserVO userVO = null;
        for (int i = 0; i < 100; i++) {
            userVO = new UserVO();
            userVO.setUserName("汪涵" + i);
            userVO.setAddress("湖南长沙");
            userVO.setAge(i + 1);
            userVOList.add(userVO);
        }
        int count = userService.addUsers(userVOList);
        System.out.println("批量插入用户信息结果||" + count);
        return "success";

    }

}
