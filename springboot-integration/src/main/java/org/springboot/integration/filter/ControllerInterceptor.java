package org.springboot.integration.filter;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springboot.integration.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect  
@Component  
public class ControllerInterceptor {    
      
    /** 
     * 定义拦截规则：拦截org.springboot.integration包下面的所有类中，有@RequestMapping注解的方法。 
     */  
    @Pointcut("execution(* org.springboot.integration.controller..*(..)) && @annotation(org.springboot.integration.filter.Token)")  
    public void controllerMethodPointcut(){}  
      
    
    @Autowired
    RedisService redisService;
    
    /** 
     * 拦截器具体实现 
     * @param pjp 
     */  
    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里  
    public Object after(ProceedingJoinPoint pjp){  
       
        Object result=null;
        
        String key= getKey(pjp);
        if(StringUtils.isNotBlank(key)){
        	
        	if(redisService.isExists(key)){
        		throw new RepeatSubmitException("不能重复提交");
        	}
        }
        redisService.set(key, "1");
        
        boolean ret=false;
        try {
        	  result = pjp.proceed();  
		} catch (Exception e) {
			ret=true;
		} catch (Throwable e) {
			ret=true;
		}
      
        if(!ret){ //执行没有出现异常，清除缓存标示
        	redisService.evict(key);
        }
        return result;  
    }  
    
    
    /**
	 * key 为空说明是不需要重复判断的方法，直接放行
	 * @param request
	 * @param handler
	 * @return
	 */
	private String getKey(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();  
	    Method method = signature.getMethod(); //获取被拦截的方法  
	    String methodName = method.getName(); //获取被拦截的方法名  
	    String className=pjp.getClass().getName();
		if(!method.isAnnotationPresent(Token.class)){
			return null;
		}
		
		Token token = method.getAnnotation(Token.class);
		
		if(StringUtils.isBlank(token.fileName())){
			return null;
		}
		
		 Object[] args = pjp.getArgs();
		 
		 for(Object arg : args){  
             System.err.println("参数:"+arg);
             if(arg instanceof HttpServletRequest){  
                 HttpServletRequest request = (HttpServletRequest) arg;  
                 //获取query string 或 posted form data参数  
                 Map<String, String[]> paramMap = request.getParameterMap();  
                 
                 for(Entry<String,String[]> entry:paramMap.entrySet()){
                 	if(entry.getKey().equals(token.fileName())){
                 		String key = "token-" +className+"-"+methodName + "-" + entry.getValue()[0];
                		System.out.println("生成的key=="+key);
                		return key;
                 	}
                 }
             }
         }  
		 
		
		return null;
		
	}
}  