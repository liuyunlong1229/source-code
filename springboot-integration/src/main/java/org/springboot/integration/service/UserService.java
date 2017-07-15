package org.springboot.integration.service;

import java.util.List;

import org.springboot.integration.common.Convertor;
import org.springboot.integration.dao.UserMapper;
import org.springboot.integration.entity.UserEntity;
import org.springboot.integration.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/17.
 */ 

@Service
@CacheConfig(cacheNames = "userCache") //相当于下面的素有的cacheable方法都配置了value的值都是userCache
public class UserService {

	
	Convertor<UserVO> convertor=Convertor.of(UserVO.class);
	Convertor<UserEntity> entityConvertor=Convertor.of(UserEntity.class);
	
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Caching()
    @Cacheable(key = "'user'+#uid")
    public UserVO getUserById(String uid) {
        UserEntity entity = userMapper.getUserById(uid);
        UserVO user=convertor.from(entity);
        return user;
    }

    public List<UserVO> findAll() {
        return userMapper.findAll();
    }

    public Long addUser(UserVO user) {
    	
    	try {
    		Thread.sleep(1000*10); //模拟后台处理时间
		} catch (Exception e) {
			// TODO: handle exception
		}
    
        UserEntity entity = entityConvertor.from(user);
        int count = userMapper.addUser(entity);
        System.out.println("影响记录条数==" + count);
        return entity.getUid();

    }
    @CacheEvict(key = "'user'+#user.getUid()")
    public int updateUserByUid(UserVO user) {
       UserEntity entity = entityConvertor.from(user);
       return userMapper.updateUserByUid(entity);
    }

    @CacheEvict(key = "'user'+#uid")
    public void deleteUser(Long uid) {
    	try {
    		Thread.sleep(1000*10); //模拟后台处理时间
		} catch (Exception e) {
			// TODO: handle exception
		}
        userMapper.deleteUser(uid);
    }


    public int addUsers(List<UserVO> users) {
        return userMapper.addUsers(users);
    }


	public UserVO getUserByName(String userName) {
		UserEntity entity=userMapper.getUserByName(userName);
		UserVO user = convertor.from(entity);
		return user;
	}
}
