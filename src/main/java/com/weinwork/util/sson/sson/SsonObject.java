package com.weinwork.util.sson.sson;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.weinwork.util.sson.attribute.FieldSerialize;
import com.weinwork.util.sson.map.FieldSerializeMap;

@Component
public class SsonObject extends SsonElement
{
	private Map<String, SsonElement> members = new HashMap<String, SsonElement>();
	
	public SsonObject()
	{
		super();
	}
	
	public SsonObject(Object obj)
	{
		super(obj);
		
		if (obj instanceof SsonObject)
		{
			
			for (Entry<String, SsonElement> elem : ((SsonObject) obj).members.entrySet())
			{
				this.add(elem.getKey(), elem.getValue());
			}
		}
		else
		{
			
			for (Field f : obj.getClass().getDeclaredFields())
			{
				
				try
				{
					f.setAccessible(true);
					this.add(f.getName(), f.get(obj));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					this.add(f.getName(), SsonNull.instance);
				}
			}
		}
	}
	
	public void add(String propertyName, SsonElement value)
	{
		members.put(propertyName, value == null ? SsonNull.instance : value);
	}
	
	public void add(String propertyName, Object object) throws SsonException
	{
		
		try
		{
			
			if (object == null)
			{
				this.add(propertyName, SsonNull.instance);
			}
			else if (object instanceof SsonObject)
			{
				this.add(propertyName, (SsonObject) object);
			}
			else if (object instanceof SsonArray)
			{
				this.add(propertyName, (SsonArray) object);
			}
			else if (object instanceof SsonPrimitive)
			{
				this.add(propertyName, (SsonPrimitive) object);
			}
			else if (object instanceof String)
			{
				this.add(propertyName, (String) object);
			}
			else if (object instanceof Number)
			{
				this.add(propertyName, (Number) object);
			}
			else if (object instanceof Boolean)
			{
				this.add(propertyName, (Boolean) object);
			}
			else if (object instanceof Character)
			{
				this.add(propertyName, (Character) object);
			}
			else if (object instanceof List)
			{
				this.add(propertyName, new SsonArray((List<?>) object));
			}
			else if (object.getClass().isArray())
			{
				this.add(propertyName, SsonUtil.reflectArrayObject(object));
			}
			else
			{
				this.add(propertyName, new SsonObject(object));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public SsonElement remove(String property)
	{
		return members.remove(property);
	}
	
	public void add(String propertyName, String value)
	{
		add(propertyName, value == null ? SsonNull.instance : new SsonPrimitive(value));
	}
	
	public void add(String propertyName, Number value)
	{
		add(propertyName, value == null ? SsonNull.instance : new SsonPrimitive(value));
	}
	
	public void add(String propertyName, Boolean value)
	{
		add(propertyName, value == null ? SsonNull.instance : new SsonPrimitive(value));
	}
	
	public void add(String propertyName, Character value)
	{
		add(propertyName, value == null ? SsonNull.instance : new SsonPrimitive(value));
	}
	
	public Set<Entry<String, SsonElement>> entrySet()
	{
		return members.entrySet();
	}
	
	public Set<String> keySet()
	{
		return members.keySet();
	}
	
	public int size()
	{
		return members.size();
	}
	
	public boolean has(String propertyName)
	{
		return members.containsKey(propertyName);
	}
	
	public SsonElement get(String propertyName)
	{
		return members.get(propertyName);
	}
	
	public SsonPrimitive getAsSsonPrimitive(String propertyName)
	{
		return (SsonPrimitive) members.get(propertyName);
	}
	
	public SsonArray getAsJsonArray(String propertyName)
	{
		return (SsonArray) members.get(propertyName);
	}
	
	public SsonObject getAsJsonObject(String propertyName)
	{
		return (SsonObject) members.get(propertyName);
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (o == this) || (o instanceof SsonObject && ((SsonObject) o).members.equals(members));
	}
	
	@Override
	public SsonObject deepCopy()
	{
		SsonObject result = new SsonObject();
		
		for (Map.Entry<String, SsonElement> entry : members.entrySet())
		{
			result.add(entry.getKey(), entry.getValue().deepCopy());
		}
		
		return result;
	}
	
}
