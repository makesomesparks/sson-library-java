package com.weinwork.util.sson.sson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.weinwork.util.sson.attribute.FieldSerialize;
import com.weinwork.util.sson.attribute.ModelSerialize;
import com.weinwork.util.sson.map.FieldSerializeMap;
import com.weinwork.util.sson.map.ModelSerializeMap;

public abstract class SsonElement
{
	final private Object object;
	
	final private ModelSerializeMap modelSerializeMap;
	
	final private Map<String, FieldSerializeMap> fieldSerializeMap = new HashMap<String, FieldSerializeMap>();
	
	public SsonElement()
	{
		this.object = new Object();
		this.modelSerializeMap = new ModelSerializeMap();
	}
	
	public SsonElement(Object obj)
	{
		
		if (obj != null)
		{
			this.object = obj;
			this.modelSerializeMap = new ModelSerializeMap(obj.getClass().getDeclaredAnnotation(ModelSerialize.class));
			
			for (Field f : obj.getClass().getDeclaredFields())
			{
				this.fieldSerializeMap.put(f.getName(), new FieldSerializeMap(f.getDeclaredAnnotation(FieldSerialize.class)));
			}
		}
		else
		{
			this.object = new Object();
			this.modelSerializeMap = new ModelSerializeMap();
		}
	}
	
	public abstract SsonElement deepCopy();
	
	public SsonArray getAsSsonArray()
	{
		
		if (this.isSsonArray())
		{
			return (SsonArray) this;
		}
		
		throw new IllegalStateException("Not a SSON Array: " + this);
	}
	
	public SsonNull getAsSsonNull()
	{
		
		if (this.isSsonNull())
		{
			return (SsonNull) this;
		}
		
		throw new IllegalStateException("Not a SSON Null: " + this);
	}
	
	public SsonObject getAsSsonObject()
	{
		
		if (this.isSsonObject())
		{
			return (SsonObject) this;
		}
		
		throw new IllegalStateException("Not a SSON Object: " + this);
	}
	
	public SsonPrimitive getAsSsonPrimitive()
	{
		
		if (this.isSsonPrimitive())
		{
			return (SsonPrimitive) this;
		}
		
		throw new IllegalStateException("Not a SSON Primitive: " + this);
	}
	
	public FieldSerializeMap getFieldSerializeMap(String propertyName)
	{
		
		if (this.fieldSerializeMap.containsKey(propertyName))
		{
			return this.fieldSerializeMap.get(propertyName);
		}
		return new FieldSerializeMap();
	}
	
	public ModelSerializeMap getModelSerializeMap()
	{
		return this.modelSerializeMap;
	}
	
	public Object getObject()
	{
		return this.object;
	}
	
	public String getSerializeFieldName(String fieldName)
	{
		FieldSerializeMap map = this.fieldSerializeMap.get(fieldName);
		
		if (map == null || map.getSerializeName().isEmpty())
		{
			return fieldName;
		}
		return map.getSerializeName();
	}
	
	public String getUserUidAttribute()
	{
		
		try
		{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			
			String userUidAttribute = SsonPropertiesReader.get(SsonPropertiesReader.PropertyName.UserUidAttribute);
			String[] attr = userUidAttribute.split("[.]");
			
			if (attr.length == 1)
			{
				return request.getAttribute(userUidAttribute).toString();
			}
			
			Object obj = request.getAttribute(attr[0]);
			
			for (int i = 1; i < attr.length; i++)
			{
				Field f = obj.getClass().getDeclaredField(attr[i]);
				
				f.setAccessible(true);
				obj = f.get(obj);
			}
			
			return obj.toString();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public boolean isSessionAdministrator()
	{
		
		try
		{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			
			String administratorAttribute = SsonPropertiesReader.get(SsonPropertiesReader.PropertyName.AdministratorAttribute);
			String[] attr = administratorAttribute.split("[.]");
			
			if (attr.length == 1)
			{
				return Boolean.parseBoolean(request.getAttribute(administratorAttribute).toString());
			}
			
			Object obj = request.getAttribute(attr[0]);
			
			for (int i = 1; i < attr.length; i++)
			{
				Field f = obj.getClass().getDeclaredField(attr[i]);
				
				f.setAccessible(true);
				obj = f.get(obj);
			}
			
			return Boolean.parseBoolean(obj.toString());
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public boolean isSsonArray()
	{
		return this instanceof SsonArray;
	}
	
	public boolean isSsonNull()
	{
		return this instanceof SsonNull;
	}
	
	public boolean isSsonObject()
	{
		return this instanceof SsonObject;
	}
	
	public boolean isSsonPrimitive()
	{
		return this instanceof SsonPrimitive;
	}
	
	public String toJson()
	{
		final StringBuilder writable = new StringBuilder();
		
		try
		{
			this.toJson(writable);
		}
		catch (final IOException caught)
		{
		}
		
		return writable.toString();
	}
	
	public void toJson(final StringBuilder writable) throws IOException
	{
		SsonSerialize.serialize(this, writable);
		SsonSerialize.removeBeforeComma(writable);
	}
}
