package org.springboot.integration.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

	TokenSource source () default TokenSource.REQUEST;

	@Deprecated
	String [] filedNames() default {};

	int  [] filedIndex() default  {};

}
