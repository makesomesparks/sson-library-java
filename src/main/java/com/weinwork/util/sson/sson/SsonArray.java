package com.weinwork.util.sson.sson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.weinwork.util.sson.map.FieldSerializeMap;

public class SsonArray extends SsonElement
{
	private ArrayList<SsonElement> elements;

	public SsonArray()
	{
		this.elements = new ArrayList<SsonElement>();
	}

	public SsonArray(Object obj)
	{
		super(obj);
		this.elements = new ArrayList<SsonElement>();
	}

	public SsonArray(List<?> list)
	{
		this.elements = new ArrayList<SsonElement>();

		for (Object o : list)
		{
			SsonObject obj = new SsonObject(o);
			this.elements.add(obj);
		}
	}

	public void add(Boolean bool)
	{
		this.elements.add(bool == null ? null : new SsonPrimitive(bool));
	}

	public void add(Character character)
	{
		this.elements.add(character == null ? SsonNull.instance : new SsonPrimitive(character));
	}

	public void add(Number number)
	{
		this.elements.add(number == null ? SsonNull.instance : new SsonPrimitive(number));
	}

	public void add(String string)
	{
		this.elements.add(string == null ? SsonNull.instance : new SsonPrimitive(string));
	}

	public void add(Object object)
	{

		if (object == null)
		{
			this.add(SsonNull.instance);
		}
		else if (object instanceof SsonObject)
		{
			this.add((SsonObject) object);
		}
		else if (object instanceof SsonArray)
		{
			this.add((SsonArray) object);
		}
		else if (object instanceof SsonPrimitive)
		{
			this.add((SsonPrimitive) object);
		}
		else if (object instanceof String)
		{
			this.add((String) object);
		}
		else if (object instanceof Number)
		{

			if (object instanceof Integer)
			{
				this.add((Integer) object);
			}
			else if (object instanceof Double)
			{
				this.add((Double) object);
			}
			else if (object instanceof Long)
			{
				this.add((Long) object);
			}
			else if (object instanceof Short)
			{
				this.add((Short) object);
			}
			else if (object instanceof Float)
			{
				this.add((Float) object);
			}
			else if (object instanceof Character)
			{
				this.add((Character) object);
			}
			else
			{
				this.add((Number) object);
			}
		}
		else if (object instanceof Boolean)
		{
			this.add((Boolean) object);
		}
		else if (object instanceof Character)
		{
			this.add((Character) object);
		}
		else if (object.getClass().isArray())
		{
			this.add(SsonUtil.reflectArrayObject(object));
		}
		else
		{
			this.add(SsonUtil.reflectToObject(object));
		}
	}

	public void add(SsonElement element)
	{

		if (element == null)
		{
			element = SsonNull.instance;
		}

		this.elements.add(element);
	}

	public boolean remove(SsonElement element)
	{
		return this.elements.remove(element);
	}

	public SsonElement remove(int index)
	{
		return this.elements.remove(index);
	}

	public void addAll(SsonArray array)
	{
		this.elements.addAll(array.elements);
	}

	public SsonElement set(int index, SsonElement element)
	{
		return this.elements.set(index, element);
	}

	public boolean contains(SsonElement element)
	{
		return this.elements.contains(element);
	}

	public int size()
	{
		return this.elements.size();
	}

	public SsonElement get(int i)
	{
		return this.elements.get(i);
	}

	public SsonArray getAsSsonArray(final int i)
	{
		return this.elements.get(i).getAsSsonArray();
	}

	public SsonObject getAsSsonObject(final int i)
	{
		return this.elements.get(i).getAsSsonObject();
	}

	public Iterator<SsonElement> iterator()
	{
		return this.elements.iterator();
	}

	@Override
	public SsonArray deepCopy()
	{

		if (!this.elements.isEmpty())
		{
			SsonArray result = new SsonArray(this.elements.size());

			for (SsonElement element : this.elements)
			{
				result.add(element.deepCopy());
			}

			return result;
		}

		return new SsonArray();
	}

	@Override
	public boolean equals(Object o)
	{
		return (o == this) || (o instanceof SsonArray && ((SsonArray) o).elements.equals(this.elements));
	}

	@Override
	public int hashCode()
	{
		return this.elements.hashCode();
	}
	
	@Override
	public FieldSerializeMap getFieldSerializeMap(String fieldName)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
