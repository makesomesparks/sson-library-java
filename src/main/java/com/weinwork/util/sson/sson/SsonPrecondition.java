package com.weinwork.util.sson.sson;

import java.math.BigInteger;

public class SsonPrecondition
{
	public static Object instanceofPrimitive(Object obj)
	{
		if (obj == null)
		{
			return SsonNull.instance;
		}
		else if (obj instanceof Byte)
		{
			return (Byte) obj;
		}
		else if (obj instanceof Short)
		{
			return (Short) obj;
		}
		else if (obj instanceof Integer)
		{
			return (Integer) obj;
		}
		else if (obj instanceof Long)
		{
			return (Long) obj;
		}
		else if (obj instanceof Float)
		{
			return (Float) obj;
		}
		else if (obj instanceof Double)
		{
			return (Double) obj;
		}
		else if (obj instanceof String)
		{
			return (String) obj;
		}
		
		return obj.toString();
	}
	
	public static Number lazyParseNumber(String str)
	{
		
		try
		{
			return Integer.parseInt(str);
		}
		catch (Exception e1)
		{
			
			try
			{
				return BigInteger.valueOf(Long.parseLong(str));
			}
			catch (Exception e2)
			{
				
				try
				{
					return Long.parseLong(str);
				}
				catch (Exception e3)
				{
					
					try
					{
						return Double.parseDouble(str);
					}
					catch (Exception e4)
					{
						
						try
						{
							return Float.parseFloat(str);
						}
						catch (Exception e5)
						{
							return (int) 0;
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parseTo(T t, Object value)
	{
		
		if (value == null)
		{
			return null;
		}
		
		if (t.equals(String.class))
		{
			
			try
			{
				return (T) String.valueOf(value);
			}
			catch (Exception e)
			{
				return (T) value.toString();
			}
		}
		else if (t.equals(Integer.class))
		{
			
			try
			{
				
				if (value instanceof String)
				{
					return (T) Integer.valueOf(((String) value));
				}
				else
				{
					return (T) Integer.valueOf(value.toString());
				}
			}
			catch (Exception e)
			{
				
			}
		}
		else if (t.equals(Long.class))
		{
			
			try
			{
				
				if (value instanceof String)
				{
					return (T) Long.valueOf(((String) value));
				}
				else
				{
					return (T) Long.valueOf(value.toString());
				}
			}
			catch (Exception e)
			{
				
			}
		}
		
		return null;
	}
	
	public static void checkArgument(boolean condition)
	{
		
		if (!condition)
		{
			throw new IllegalArgumentException();
		}
	}
	
}
