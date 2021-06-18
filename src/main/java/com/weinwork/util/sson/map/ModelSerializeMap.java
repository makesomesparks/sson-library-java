package com.weinwork.util.sson.map;

import com.weinwork.util.sson.attribute.ModelSerialize;
import com.weinwork.util.sson.attribute.ModelSerialize.ExposeRule;

public class ModelSerializeMap
{
	ExposeRule exposeRule;
	boolean disposeChildrenNull;
	
	public ModelSerializeMap()
	{
		this.exposeRule = ExposeRule.ALWAYS;
		this.disposeChildrenNull = false;
	}
	
	public ModelSerializeMap(ModelSerialize modelSerialize)
	{
		/*
		 * this.userUidAttributeName = SsonPropertiesReader.get(SsonPropertiesReader.PropertyName.UserUidAttribute);
		 * this.sessionAdministratorAttributeName =
		 * SsonPropertiesReader.get(SsonPropertiesReader.PropertyName.AdministratorAttribute);
		 */
		
		if (modelSerialize != null)
		{
			this.exposeRule = modelSerialize.exposeRule();
			this.disposeChildrenNull = modelSerialize.disposeChildrenNull();
		}
		else
		{
			this.exposeRule = ExposeRule.ALWAYS;
			this.disposeChildrenNull = false;
		}
	}
	
	public ExposeRule getExposeRule()
	{
		return exposeRule;
	}
	
	public void setExposeRule(ExposeRule exposeRule)
	{
		this.exposeRule = exposeRule;
	}
	
}
