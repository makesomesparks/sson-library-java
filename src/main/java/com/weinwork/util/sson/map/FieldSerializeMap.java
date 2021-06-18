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
	String serializeName;
	boolean paramIgnore;
	boolean paramRequired;
	boolean userUidField;
	boolean isDisposeNull;
	ExposeRule exposeRule;
	
	public FieldSerializeMap()
	{
		this.paramName = "";
		this.serializeName = "";
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
			this.serializeName = fieldSerialize.jsonName();
			this.paramIgnore = fieldSerialize.paramIgnore();
			this.paramIgnore = fieldSerialize.paramRequired();
			this.exposeRule = fieldSerialize.exposeRule();
			this.userUidField = fieldSerialize.userUid();
			this.isDisposeNull = fieldSerialize.disposeNull();
		}
		else
		{
			this.paramName = "";
			this.serializeName = "";
			this.paramIgnore = false;
			this.paramRequired = false;
			this.userUidField = false;
			this.isDisposeNull = false;
			this.exposeRule = ExposeRule.ALWAYS;
		}
	}
}
