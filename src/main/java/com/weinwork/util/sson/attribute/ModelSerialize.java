package com.weinwork.util.sson.attribute;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.weinwork.util.sson.rule.ExposeRule;
import com.weinwork.util.sson.sson.SsonPropertiesReader;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelSerialize
{
	ExposeRule exposeRule() default ExposeRule.ALWAYS;
	boolean disposeChildrenNull() default false;
}