package org.springboot.integration.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springboot.integration.dao.UserMapper;
import org.springboot.integration.entity.UserEntity;
import org.springboot.integration.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/17.
 */

@Service
@CacheConfig(cacheNames = "userCache")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Cacheable(key = "'user'+#uid")
    public UserVO getUserById(String uid) {
        System.out.println("执行了查询数据库的方法7777");
        UserEntity entity = userMapper.getUserById(uid);
        UserVO user = new UserVO();
        try {
            BeanUtils.copyProperties(user, entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return user;
    }


    public List<UserVO> findAll() {
        return userMapper.findAll();
    }

    public Long addUser(UserVO user) {
        UserEntity entity = new UserEntity();
        try {
            BeanUtils.copyProperties(entity, user);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int count = userMapper.addUser(entity);
        System.out.println("影响记录条数==" + count);
        return entity.getUid();

    }
    @CacheEvict(key = "'user'+#user.getUid()")
    public int updateUserByUid(UserVO user) {
        UserEntity entity = new UserEntity();
        try {
            BeanUtils.copyProperties(entity, user);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
       return userMapper.updateUserByUid(entity);
    }

    @CacheEvict(key = "'user'+#uid")
    public void deleteUser(Long uid) {
        userMapper.deleteUser(uid);
    }


    public int addUsers(List<UserVO> users) {
        return userMapper.addUsers(users);
    }


	public UserVO getUserByName(String userName) {
		UserEntity entity=userMapper.getUserByName(userName);
		
		if(entity == null){
			return null;
		}
		UserVO user=new UserVO();
		 try {
	            BeanUtils.copyProperties(entity, user);
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
		 
		 return user;
	}
}
