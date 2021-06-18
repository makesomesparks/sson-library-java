package com.weinwork.util.sson.sson;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.weinwork.util.sson.map.FieldSerializeMap;
import com.weinwork.util.sson.map.ModelSerializeMap;

public class SsonPrimitive extends SsonElement
{
	private final Object value;
	
	public SsonPrimitive(Object v)
	{
		super(v);
		value = SsonPrecondition.instanceofPrimitive(v);
	}
	
	public Object value()
	{
		if (this.isBoolean())
		{
			return this.getAsBoolean();
		}
		else if (this.isSsonNull())
		{
			return null;
		}
		else if (this.isNumber())
		{
			return this.getAsNumber();
		}
		else if (this.isString())
		{
			return this.getAsString();
		}
		
		return this.value();
	}
	
	@Override
	public SsonPrimitive deepCopy()
	{
		return this;
	}
	
	public boolean isBoolean()
	{
		return value instanceof Boolean;
	}
	
	public boolean isNumber()
	{
		return value instanceof Number;
	}
	
	public boolean isDouble()
	{
		return value instanceof Double;
	}
	
	public boolean isByte()
	{
		return value instanceof Byte;
	}
	
	public boolean isShort()
	{
		return value instanceof Short;
	}
	
	public boolean isLong()
	{
		return value instanceof Long;
	}
	
	public boolean isInteger()
	{
		return value instanceof Integer;
	}
	
	public boolean isFloat()
	{
		return value instanceof Float;
	}
	
	public boolean isCharacter()
	{
		return value instanceof Character;
	}
	
	public boolean isString()
	{
		return value instanceof String;
	}
	
	public boolean isNull()
	{
		return value instanceof SsonNull;
	}
	
	public boolean isSsonNull()
	{
		return this.isNull();
	}
	
	public Boolean getAsBoolean()
	{
		
		if (isBoolean())
		{
			return ((Boolean) value).booleanValue();
		}
		
		return Boolean.parseBoolean(getAsString());
	}
	
	public Number getAsNumber()
	{
		
		if (isNumber())
		{
			return ((Number) this.value);
		}
		
		return null;
	}
	
	public String getAsString()
	{
		
		if (isNumber())
		{
			return getAsNumber().toString();
		}
		else if (isBoolean())
		{
			return ((Boolean) value).toString();
		}
		else
		{
			return (String) value;
		}
	}
	
	public Double getAsDouble()
	{
		return this.getAsDouble(null);
	}
	
	public Double getAsDouble(Double defaultValue)
	{
		
		if (value instanceof Double)
		{
			return (Double) this.value;
		}
		else if (value instanceof Number)
		{
			return (Double) this.parseTo(Double.class);
		}
		
		if (defaultValue == null)
		{
			return null;
		}
		else
		{
			return defaultValue;
		}
	}
	
	public BigDecimal getAsBigDecimal()
	{
		
		if (value instanceof BigDecimal)
		{
			
		}
		
		return value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal(value.toString());
	}
	
	public BigInteger getAsBigInteger()
	{
		return value instanceof BigInteger ? (BigInteger) value : new BigInteger(value.toString());
	}
	
	public Float getAsFloat()
	{
		return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
	}
	
	public Long getAsLong()
	{
		return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
	}
	
	public Short getAsShort()
	{
		return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
	}
	
	public Integer getAsInt()
	{
		return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
	}
	
	public Byte getAsByte()
	{
		return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
	}
	
	public Character getAsCharacter()
	{
		return getAsString().charAt(0);
	}
	
	@Override
	public int hashCode()
	{
		
		if (value == null)
		{
			return 31;
		}
		
		if (isIntegral(this))
		{
			long value = getAsNumber().longValue();
			return (int) (value ^ (value >>> 32));
		}
		
		if (value instanceof Number)
		{
			long value = Double.doubleToLongBits(getAsNumber().doubleValue());
			return (int) (value ^ (value >>> 32));
		}
		
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		SsonPrimitive other = (SsonPrimitive) obj;
		
		if (value == null)
		{
			return other.value == null;
		}
		
		if (isIntegral(this) && isIntegral(other))
		{
			return getAsNumber().longValue() == other.getAsNumber().longValue();
		}
		
		if (value instanceof Number && other.value instanceof Number)
		{
			double a = getAsNumber().doubleValue();
			double b = other.getAsNumber().doubleValue();
			return a == b || (Double.isNaN(a) && Double.isNaN(b));
		}
		
		return value.equals(other.value);
	}
	
	private static boolean isIntegral(SsonPrimitive primitive)
	{
		
		if (primitive.value instanceof Number)
		{
			Number number = (Number) primitive.value;
			return number instanceof BigInteger || number instanceof Long || number instanceof Integer
					|| number instanceof Short || number instanceof Byte;
		}
		
		return false;
	}
	
	private Object parseTo(Object t) throws ClassCastException
	{
		
		/// Number
		if(t instanceof Number)
		{
			Double number = Double.parseDouble(String.valueOf(this.value));
			
			if (Byte.class.equals(t.getClass()))
			{
				return number.byteValue();
			}
			else if (Short.class.equals(t.getClass()))
			{
				return number.shortValue();
			}
			else if (Integer.class.equals(t.getClass()))
			{
				return number.intValue();
			}
			else if (Long.class.equals(t.getClass()))
			{
				return number.longValue();
			}
			else if (Float.class.equals(t.getClass()))
			{
				return number.floatValue();
			}
			else if (Double.class.equals(t.getClass()))
			{
				return number.doubleValue();
			}
			
		}
		
		throw new ClassCastException(this.value.getClass().getName() + " class not support");
		
	}

	@Override
	public FieldSerializeMap getFieldSerializeMap(String fieldName)
	{
		return new FieldSerializeMap();
	}
}
