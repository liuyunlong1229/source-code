package org.springboot.integration.filter;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springboot.integration.exception.RepeatSubmitException;
import org.springboot.integration.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

@Aspect
@Component
public class ControllerInterceptor {

	/**
	 * 定义拦截规则：拦截org.springboot.integration包下 面的所有类中有注解token的
	 * 由于之前声明此切入点是controller中token方法，但是一直controller中所有的方法都会进入，
	 * 后面将代理模式改成spring.aop.proxy-target-class=true后，就能对只是注解了token的方法进行拦截了
	 */
	@Pointcut("execution(* org.springboot.integration.controller..*(..)) && @annotation(org.springboot.integration.filter.Token)")
	public void controllerMethodPointcut() {
	}

	@Autowired
	RedisService redisService;

	/**
	 * 拦截器具体实现
	 * 
	 * @param pjp
	 */
	@Around("controllerMethodPointcut()")
	public Object around(ProceedingJoinPoint pjp) {

		Object result = null;

		String key = getKey(pjp);
		if (StringUtils.isNotBlank(key)) {
			if (redisService.isExists(key)) {
				throw new RepeatSubmitException("不能重复提交");
			}
			redisService.set(key, "1");
			try {
				result = pjp.proceed();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}finally{
				redisService.evict(key); //不管执行成功还是失败都会执行
			}
			
		}
		
		return result;
	}

	/**
	 * key 为空说明是不需要重复判断的方法，直接放行
	 * 
	 * @param pjp
	 * @return
	 */
	private String getKey(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法
		String methodName = method.getName(); // 获取被拦截的方法名
		
		String className = pjp.getTarget().getClass().getName();
		//System.err.println(className+":"+methodName);
		
		/*if (!method.isAnnotationPresent(Token.class)) {
			return null;
		}
*/
		Token token = method.getAnnotation(Token.class);

		Object[] args = pjp.getArgs();

		String key = null;
		if (token.source()==TokenSource.FILED) {
			key = getKey1(args,token.filedIndex());
		} else {
			key = getKey2(args);
		}
		
		if(key ==null){
			return  null;
		}
		key="TOKEN-"+className+":"+methodName+key;
		System.err.println("生成的key=="+key);
		return key;

	}


	//由于在执行时，通过getParamter获取，无法得到参数名称，只能得到arg0,arg1等
	/*private String getKey1(Parameter[] pms,Object[] args,String [] filedNames) {

		List<String> fileds= Arrays.asList(filedNames);
		StringBuffer buffer=new StringBuffer();

		for(int i=0;i<pms.length;i++){
			Parameter pm=pms[i];
			System.out.println(pm.getName()+"=="+args[i].toString());
			if(fileds.contains(pm.getName())){
				buffer.append("-"+args[i].toString());
			}

		}

		return buffer.toString();
	}*/


	private String getKey1(Object[] args,int [] filedIndexs) {

		if(filedIndexs.length==0){
			throw new RuntimeException("token.filedIndex() can not be empty");
		}
		StringBuffer buffer=new StringBuffer();

		for(int i=0;i<filedIndexs.length;i++){

			if(filedIndexs[i]>args.length-1){
				throw new RuntimeException("token.filedIndex() contain error index");
			}
			buffer.append("-"+args[filedIndexs[i]].toString());
		}


		return buffer.toString();
	}



	private String getKey2(Object[] args) {

		boolean hasReqeustParam=false;
		StringBuffer buffer = new StringBuffer();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				hasReqeustParam=true;
				HttpServletRequest request = (HttpServletRequest) arg;
				Map<String, String[]> paramMap = request.getParameterMap();
				for (Entry<String, String[]> entry : paramMap.entrySet()) {
					buffer.append("-"+entry.getValue()[0]);
				}
				return buffer.toString();
			}

		}

		if(!hasReqeustParam){
			throw new RuntimeException("params must has HttpServletRequest");
		}

		return null;
	}
}