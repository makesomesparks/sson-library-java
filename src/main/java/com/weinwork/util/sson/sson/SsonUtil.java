package com.weinwork.util.sson.sson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SsonUtil
{
	public static Object deepClone(Object object)
	{
		
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			
			baos.close();
			oos.close();
			
			Object result = ois.readObject();
			
			bais.close();
			ois.close();
			
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static SsonArray reflectArrayObject(Object obj)
	{
		SsonArray sson = new SsonArray();
		Class<?> ofArray = obj.getClass().getComponentType();
		Object[] list;
		
		if (ofArray.isPrimitive())
		{
			int length = Array.getLength(obj);
			list = new Object[length];
			
			for (int i = 0; i < length; i++)
			{
				list[i] = Array.get(obj, i);
			}
		}
		else
		{
			list = (Object[]) obj;
		}
		
		for (Object o : list)
		{
			sson.add(o);
		}
		
		return sson;
	}
	
	public static SsonElement reflectToObject(Object obj)
	{
		SsonObject sson = new SsonObject();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field f : fields)
		{
			
			try
			{
				boolean accessible = f.isAccessible();
				
				f.setAccessible(true);
				
				sson.add(f.getName(), f.get(obj));
				
				f.setAccessible(accessible);
			}
			catch (Exception e)
			{
				return new SsonNull();
			}
			
		}
		
		return sson;
	}
	
}
