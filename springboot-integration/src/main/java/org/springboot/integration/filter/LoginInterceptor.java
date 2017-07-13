package org.springboot.integration.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class LoginInterceptor implements HandlerInterceptor {

	public static final String SESSION_USER="session_user";
	public static final int SESSION_TIME_OUT=444;
	
	private String[]	ignoreUrlArr=new String[]{"/login","/user/login"};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpServletRequest req = request;
		HttpServletResponse resp = response;
		//System.err.println("LoginInterceptor的preHandle方法");
		String reqURI = req.getRequestURI();
		String requestType = request.getHeader("X-Requested-With");
		//System.err.println("reqURL==" + reqURI + "    reqType==" + requestType);
		
		Object user=req.getSession().getAttribute(LoginInterceptor.SESSION_USER);
		if (user!= null) {
			return true;
		}
		
		reqURI=reqURI.replaceAll(req.getContextPath(), "");
		if (isIgnoreURL(reqURI)) {
			return true;
		}
		if (isAjaxRequest(req)) {
			resp.sendError(SESSION_TIME_OUT);
		} else {
			resp.sendRedirect(req.getContextPath() + "/login");
		}

		return false;
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

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//System.err.println("LoginInterceptor的postHandle方法");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//System.err.println("LoginInterceptor的afterCompletion方法");
	}

	public String[] getIgnoreUrlArr() {
		return ignoreUrlArr;
	}

	public void setIgnoreUrlArr(String[] ignoreUrlArr) {
		this.ignoreUrlArr = ignoreUrlArr;
	}

}
