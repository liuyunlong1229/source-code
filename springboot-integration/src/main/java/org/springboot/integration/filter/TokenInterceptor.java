package org.springboot.integration.filter;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springboot.integration.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class TokenInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		 Map<String, String[]> paramMap = request.getParameterMap();  
		for(Entry<String, String[]> entry:paramMap.entrySet()){
			
			
			System.err.println("entry.getKey();==="+entry.getKey()+"|| entry.getValue()"+entry.getValue());
		}
		
		if (handler instanceof HandlerMethod) {
				String key=this.getKey(request, (HandlerMethod)handler);
				if(key ==null){
					return true;
				}
				if (redisService.isExists(key)) {
					System.err.println("不允许重复提交");
					return false;
				}
				redisService.set(key,"1");
				return true;
			}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.err.println("LoginInterceptor的postHandle方法");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
			if (ex == null) {
			if (handler instanceof HandlerMethod) {
				String key=this.getKey(request, (HandlerMethod)handler);
				if(key !=null){
					redisService.evict(key);
				}
			}
		}
	}

	
	/**
	 * key 为空说明是不需要重复判断的方法，直接放行
	 * @param request
	 * @param handler
	 * @return
	 */
	private String getKey(HttpServletRequest request, HandlerMethod handler) {
		Method method = handler.getMethod();
		if(!method.isAnnotationPresent(Token.class)){
			return null;
		}
		
		Token token = method.getAnnotation(Token.class);
		
		if(StringUtils.isBlank(token.fileName())){
			return null;
		}
		String tokenFiled = request.getParameter(token.fileName());
		String key = "token-" + method.getName() + "-" + tokenFiled;
		System.out.println("生成的key=="+key);
		return key;
		
		
		}

}
