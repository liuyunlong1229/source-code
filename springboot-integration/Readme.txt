http://blog.csdn.net/wwkms/article/details/48851005  自定义时间的定时任务。
http://blog.csdn.net/shandon100/article/details/53479986 jredisCluster集群
http://blog.csdn.net/catoop/article/details/50501706 静态资源处理


设置了contextPath那应用下所有的资源都只能在contextPath目录下访问了，可以使用反向代理来重写URL也是可以做到的。
当然你如果将contextPath设置为/在api前面增加前缀，这种方式会比较简单。

比如下面的这种方式。增加一个servlet专门用于api地址访问。

@Bean
public ServletRegistrationBean apiV1ServletBean(WebApplicationContext wac) {
    DispatcherServlet ds = new DispatcherServlet(wac);
    ServletRegistrationBean bean = new ServletRegistrationBean(ds, "/api/v1/*");
    bean.setName("api-v1");
    return bean;
}

https://stackoverflow.com/questions/24242554/what-is-the-syntax-to-get-thymeleaf-pagecontext-request-contextpath


SESSION的生命周期：
当第一个访问服务器的时候，在也没响应后，会在cookie中生成一个session
当退出浏览器后，在次进入时，之前是session会丢失。
同一个浏览器的不同的页签session会共享。
