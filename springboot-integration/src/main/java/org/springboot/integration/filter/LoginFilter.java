package org.springboot.integration.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;


/**
  * 使用注解标注过滤器
  * @WebFilter将一个实现了javax.servlet.Filte接口的类定义为过滤器
  * 属性filterName声明过滤器的名称,可选
  * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
  */
//@WebFilter(filterName="LoginFilter",urlPatterns="/*")
public class LoginFilter implements Filter {

	
public static final String SESSION_USER="session_user";
	
	private String[]	ignoreUrlArr=new String[]{"/user/login","/login"};
	
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.err.println("过滤器初始化..........");
		
	}
	
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String reqURI = req.getRequestURI();
		String requestType = req.getHeader("X-Requested-With");
		System.err.println("reqURL==" + reqURI + "    reqType==" + requestType);
		
		Object user=req.getSession().getAttribute(LoginFilter.SESSION_USER);
		if (user== null) {
			
			if (!isIgnoreURL(reqURI)) {
				
				if (isAjaxRequest(req)) {
					resp.sendError(HttpStatus.UNAUTHORIZED.value());
				} else {
					resp.sendRedirect(req.getContextPath() + "/login");
				}
			}
		}
		
		chain.doFilter(request, response);
		
	}

	
	

	@Override
	public void destroy() {
		System.err.println("过滤器被销毁.........");
	}

	
	private boolean isIgnoreURL(String reqUrl) {
		for (String ignoreUrl : ignoreUrlArr) {
			if(reqUrl.contains(ignoreUrl)){
				return true;
			}
		}
		return false;
	}
	

	private boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		return requestType != null && requestType.equals("XMLHttpRequest");
	}

}
