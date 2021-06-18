package com.weinwork.util.sson.util;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import com.weinwork.util.sson.map.FieldSerializeMap;

public class ParamNameBindUtil extends ExtendedServletRequestDataBinder
{
	private final Map<String, FieldSerializeMap> paramMappings;
	
	public ParamNameBindUtil(Object target, String objectName, Map<String, FieldSerializeMap> paramMappings)
	{
		super(target, objectName);
		this.paramMappings = paramMappings;
	}
	
	@Override
	protected void addBindValues(MutablePropertyValues mutablePropertyValues, ServletRequest request)
	{
		super.addBindValues(mutablePropertyValues, request);
		
		for (Map.Entry<String, FieldSerializeMap> entry : paramMappings.entrySet())
		{
			String parameter = entry.getKey();
			String paramName = entry.getValue().getParamName();
			
			if (mutablePropertyValues.contains(parameter))
			{
				mutablePropertyValues.add(paramName, mutablePropertyValues.getPropertyValue(parameter).getValue());
			}
			else
			{
				// @FIXME how to throw requied exception?
				
				if (entry.getValue().isParamRequired())
				{
				}
			}
		}
	}
}
