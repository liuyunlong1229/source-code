package org.springboot.integration.config;

import org.springboot.integration.filter.LoginInterceptor;
import org.springboot.integration.filter.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration   //标注此文件为一个配置项，spring boot才会扫描到该配置。该注解类似于之前使用xml进行配置
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	
	 @Bean  
	 public TokenInterceptor getTokenInterceptor() {  
	        return new TokenInterceptor();  
	  }  
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");  //对来自/user/** 这个链接来的请求进行拦截
        //registry.addInterceptor(getTokenInterceptor()).addPathPatterns("/**");  //对来自/user/** 这个链接来的请求进行拦截
        super.addInterceptors(registry);
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  registry.addResourceHandler("/myres/**").addResourceLocations("file:F:/myimgs/");
		super.addResourceHandlers(registry);
	}
    
    
}