package com.weinwork.util.sson.map;

import com.weinwork.util.sson.attribute.FieldSerialize;
import com.weinwork.util.sson.rule.ExposeRule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldSerializeMap
{
	String paramName;
	String exposeName;
	boolean paramIgnore;
	boolean paramRequired;
	boolean userUidField;
	boolean isDisposeNull;
	ExposeRule exposeRule;
	
	public FieldSerializeMap()
	{
		this.paramName = "";
		this.exposeName = "";
		this.paramIgnore = false;
		this.paramRequired = false;
		this.userUidField = false;
		this.isDisposeNull = false;
		this.exposeRule = ExposeRule.ALWAYS;
	}
	
	public FieldSerializeMap(FieldSerialize fieldSerialize)
	{
		if(fieldSerialize != null)
		{
			this.paramName = fieldSerialize.paramName();
			this.exposeName = fieldSerialize.exposeName();
			this.paramIgnore = fieldSerialize.paramIgnore();
			this.paramRequired = fieldSerialize.paramRequired();
			this.exposeRule = fieldSerialize.exposeRule();
			this.userUidField = fieldSerialize.userUid();
			this.isDisposeNull = fieldSerialize.disposeNull();
		}
		else
		{
			this.paramName = "";
			this.exposeName = "";
			this.paramIgnore = false;
			this.paramRequired = false;
			this.userUidField = false;
			this.isDisposeNull = false;
			this.exposeRule = ExposeRule.ALWAYS;
		}
	}
}
