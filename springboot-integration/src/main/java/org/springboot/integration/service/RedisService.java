package org.springboot.integration.service;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springboot.integration.handler.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/18.
 */

@Service
public class RedisService {

	    @Autowired  
	    private RedisTemplate<String, ?> redisTemplate;  
	      
	    public boolean set(final String key, final String value) {  
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                connection.set(serializer.serialize(key), serializer.serialize(value));  
	                return true;  
	            }  
	        });  
	        return result;  
	    }  
	  
	    public String get(final String key){  
	        String result = redisTemplate.execute(new RedisCallback<String>() {  
	            @Override  
	            public String doInRedis(RedisConnection connection) throws DataAccessException {  
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                byte[] value =  connection.get(serializer.serialize(key));  
	                return serializer.deserialize(value);  
	            }  
	        });  
	        return result;  
	    }  
	    
	    public boolean isExists(final String key){
	    	return redisTemplate.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection) throws DataAccessException { 
	            	 RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                 return connection.exists(serializer.serialize(key));
	            }  
	        });  
	    }
	  
	 
	    public boolean expire(final String key, long expire) {  
	        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);  
	    }  
	    
	    
	    public void evict(String key){
	    	redisTemplate.delete(key);
	    }
	    
	    public Long dbSize() {  
	    	
	    	return redisTemplate.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection) throws DataAccessException { 
	                 return connection.dbSize();
	            }  
	        });  
	    	
	    }  
	  
	    /** 
	     * 删除所有指定数据库的数据 
	     */  
	    public Boolean flushDB() {  
	    	return redisTemplate.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection) throws DataAccessException { 
	                 connection.flushDb();
	                 return true;
	            }  
	        });
	    }  
	
	    public <T> boolean setList(String key, List<T> list) {  
	        String value = JSONUtil.toJson(list);  
	        return set(key,value);  
	    }  
	  
	    public <T> List<T> getList(String key,Class<T> clz) {  
	        String json = get(key);  
	        if(json!=null){  
	            List<T> list = JSONUtil.toList(json, clz);  
	            return list;  
	        }  
	        return null;  
	    }  
	  
	   
}




