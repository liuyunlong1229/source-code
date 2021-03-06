package org.springboot.integration.filter;

/**
  * 使用注解标注过滤器
  * @WebFilter将一个实现了javax.servlet.Filte接口的类定义为过滤器
  * 属性filterName声明过滤器的名称,可选
  * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
  */


/*
@Configuration
@WebFilter(filterName="LoginFilter",urlPatterns="*//*
)

*/

public class LoginFilter  {

 /**implements Filter

	
public static final String SESSION_USER="session_user";
	
	private String[]	ignoreUrlArr=new String[]{"/user/login","/login"};
	
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		//System.err.println("过滤器初始化..........");
		
	}
	
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request;
			String reqURI = req.getRequestURI();
			String requestType = req.getHeader("X-Requested-With");
			//System.err.println("拦截的方法执行开始===过滤器里面的拦截的 reqURL==" + reqURI + "    reqType==" + requestType);
		    chain.doFilter(request, response);
		   // System.err.println("拦截的方法执行完了===过滤器里面的拦截的 reqURL==" + reqURI + "    reqType==" + requestType);
		
	}

	
	

	@Override
	public void destroy() {
		System.err.println("过滤器被销毁.........");
	}
*/

}
