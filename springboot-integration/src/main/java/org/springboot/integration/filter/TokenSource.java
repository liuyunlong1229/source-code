package org.springboot.integration.filter;

/**
 * Created by Administrator on 2017/7/16.
 */

/**
 * REQUEST 表示从参数列表，HttpServletRequest中构造token
 * FILED  表示从参数列表中构造token,此时必须指定参数名称
 */
public enum TokenSource {

        REQUEST,FILED
}
