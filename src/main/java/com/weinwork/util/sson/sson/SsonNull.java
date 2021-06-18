package com.weinwork.util.sson.sson;

import com.weinwork.util.sson.map.FieldSerializeMap;

public class SsonNull extends SsonElement
{
	public static final SsonNull instance = new SsonNull();
	
	public SsonNull()
	{
	}
	
	@Override
	public SsonNull deepCopy()
	{
		return instance;
	}
	
	@Override
	public int hashCode()
	{
		return SsonNull.class.hashCode();
	}
	
	@Override
	public boolean equals(Object other)
	{
		return this == other || other instanceof SsonNull;
	}

	@Override
	public FieldSerializeMap getFieldSerializeMap(String fieldName)
	{
		return null;
	}
}
