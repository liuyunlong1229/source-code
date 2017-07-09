package org.springboot.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


//开启session纳入redis管理的模式
@Configuration  
@EnableRedisHttpSession
public class RedisSessionConfig {

}
