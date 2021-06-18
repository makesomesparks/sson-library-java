package com.weinwork.util.sson.attribute;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelSerialize
{
	public static enum ExposeRule
	{
		ALWAYS, HIDE, ONLY_USER_UID, NOT_USER_UID, ONLY_ADMINISTRATOR
	}
	
	ExposeRule exposeRule() default ExposeRule.ALWAYS;
	
	boolean disposeChildrenNull() default false;
}
