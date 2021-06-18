package com.weinwork.util.sson.attribute;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.weinwork.util.sson.rule.ExposeRule;

@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSerialize
{
	String paramName() default "";

	boolean paramRequired() default false;

	boolean paramIgnore() default false;

	boolean userUid() default false;
	
	String exposeName() default "";
	
	boolean disposeNull() default false;

	ExposeRule exposeRule() default ExposeRule.ALWAYS;
}