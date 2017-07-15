package org.springboot.integration.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springboot.integration.filter.LoginFilter;
import org.springboot.integration.filter.Token;
import org.springboot.integration.service.RedisService;
import org.springboot.integration.service.UserService;
import org.springboot.integration.util.StringDateUtil;
import org.springboot.integration.vo.UserVO;
import org.springboot.integration.web.Result;
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
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    static Map<Long, UserVO> users = Collections.synchronizedMap(new HashMap<Long, UserVO>());

    
    @ApiOperation(value = "用户登录", notes = "根据用户名查询用户")
    @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", required = true)
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Result login(String userName,HttpServletRequest request){
    	UserVO user=userService.getUserByName(userName);
    	if(user !=null){
    		request.getSession().setAttribute(LoginFilter.SESSION_USER, user);
    		return Result.OK;
    	}
    	return Result.fail("用户名存在");
    	
    }
    @ApiOperation(value = "获取用户列表", notes = "查询所有用户信息")
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public Result getUsers(HttpServletRequest request) {
        List<UserVO>  userList= redisService.getList("userList", UserVO.class);
        if(userList == null){
        	 System.out.println("缓存中没有用户列表，准备查询数据库了。。。。。");
        	 userList=userService.findAll();
             redisService.setList("userList", userList);
        }
       /* for(UserVO x:userList){
        	System.out.println(x.getUserName()+"||"+x.getBirthDay().getTime()+"||"+StringDateUtil.dateToString(x.getBirthDay(),4));
        }*/
      
        return  Result.ok(userList);

    }

    @ApiOperation(value = "查询用户详情", notes = "根据url中的用户编码查询", response = UserVO.class)
    @ApiImplicitParam(name = "uid", value = "编码", paramType = "path", required = true)
    @RequestMapping(value = "/user/{uid}", method = RequestMethod.GET)
    public Result getUser(@PathVariable("uid") String userid) {
        UserVO userVo= userService.getUserById(userid);
        return  Result.ok(userVo);

    }

    @ApiOperation(value = "新增用户", notes = "根据用户信息新增用户")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "userName", value = "名称", paramType = "query", required = true),
         @ApiImplicitParam(name = "age", value = "性别", paramType = "query", required = true),
         @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true)
    })
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @Token
    public Result addUser(HttpServletRequest requst,UserVO user) {
    	
    	UserVO vo=userService.getUserByName(user.getUserName());
    	if(vo !=null){
    		return Result.fail("用户已存在，不能重复添加");
    	}
    	
    	user.setBirthDay(new Date());
        Long uid = userService.addUser(user);
        redisService.evict("userList");
        return Result.OK;

    }


    @ApiOperation(value = "修改用户", notes = "根据用户编码修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "编码", paramType = "query", required = true),
            @ApiImplicitParam(name = "userName", value = "名称", paramType = "query", required = true),
            @ApiImplicitParam(name = "age", value = "性别", paramType = "query", required = true),
            @ApiImplicitParam(name = "address", value = "地址", paramType = "query", required = true)
    })
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @Token
    public Result updateUser(HttpServletRequest requst,UserVO user) {
        System.out.println("用户编号||" + user.getUid());
        userService.updateUserByUid(user);
        return Result.OK;

    }


    @ApiOperation(value = "删除用户", notes = "根据用户编码删除用户")
    @ApiImplicitParam(name = "uid", value = "编码", paramType = "path", required = true)
    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    @Token
    public Result delUser(HttpServletRequest requst ,@PathVariable Long uid) {
        userService.deleteUser(uid);
        return Result.OK;

    }


    @ApiOperation(value = "批量增加用户")
    @RequestMapping(value = "/user/batch", method = RequestMethod.POST)
    public Result addUserBatch() {
        List<UserVO> userVOList = new ArrayList<UserVO>();
        UserVO userVO = null;
        for (int i = 0; i < 10; i++) {
            userVO = new UserVO();
            userVO.setUserName("汪涵" + i);
            userVO.setAddress("湖南长沙");
            userVO.setBirthDay(StringDateUtil.addMonth(new Date(), -i));
            userVO.setAge(i + 1);
            userVOList.add(userVO);
            
            
        }
        int count = userService.addUsers(userVOList);
        System.out.println("批量插入用户信息结果||" + count);
        return Result.OK;

    }
    
    
    @ApiOperation(value = "退出系统")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public Result logout(HttpServletRequest request) {
    	 request.getSession().invalidate();
    	 return Result.OK;

    }

}
