package com.weinwork.util.sson.sson;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map.Entry;
import com.weinwork.util.sson.map.FieldSerializeMap;
import com.weinwork.util.sson.map.ModelSerializeMap;
import com.weinwork.util.sson.ylex.Yylex;
import com.weinwork.util.sson.ylex.Yytoken;

public class SsonSerialize
{
	private enum States
	{
		DONE, INIT, ERROR, ARRAY, ENTRY, OBJECT
	}
	
	public static void removeBeforeComma(final StringBuilder writable)
	{
		
		while (writable.charAt(writable.length() - 1) == ',')
		{
			writable.deleteCharAt(writable.length() - 1);
		}
	}
	
	private static SsonElement deserialize(final Reader deserializable) throws SsonException
	{
		final Yylex lexer = new Yylex(deserializable);
		Yytoken token;
		States currentState;
		int returnCount = 1;
		final LinkedList<States> stateStack = new LinkedList<>();
		final LinkedList<Object> valueStack = new LinkedList<>();
		stateStack.addLast(States.INIT);
		
		do
		{
			currentState = SsonSerialize.popNextState(stateStack);
			token = SsonSerialize.lexNextToken(lexer);
			
			switch (currentState)
			{
				case DONE:
					
					if (Yytoken.Types.END.equals(token.getType()))
					{
						break;
					}
					
					returnCount += 1;
					break;
				
				case INIT:
					
					switch (token.getType())
					{
						case DATUM:
							valueStack.addLast(token.getValue());
							stateStack.addLast(States.DONE);
							
							break;
						
						case BRACE_L:
							valueStack.addLast(new SsonObject());
							stateStack.addLast(States.OBJECT);
							
							break;
						
						case BRACKET_L:
							valueStack.addLast(new SsonArray());
							stateStack.addLast(States.ARRAY);
							
							break;
						
						default:
							throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
					}
					
					break;
				
				case ERROR:
					throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
				
				case ARRAY:
					switch (token.getType())
					{
						case COMMA:
							stateStack.addLast(currentState);
							break;
						
						case DATUM:
							SsonArray val = (SsonArray) valueStack.getLast();
							val.add(token.getValue());
							stateStack.addLast(currentState);
							break;
						
						case BRACE_L:
							val = (SsonArray) valueStack.getLast();
							final SsonObject object = new SsonObject();
							val.add(object);
							valueStack.addLast(object);
							stateStack.addLast(currentState);
							stateStack.addLast(States.OBJECT);
							break;
						
						case BRACKET_L:
							val = (SsonArray) valueStack.getLast();
							final SsonArray array = new SsonArray();
							val.add(array);
							valueStack.addLast(array);
							stateStack.addLast(currentState);
							stateStack.addLast(States.ARRAY);
							break;
						
						case BRACKET_R:
							if (valueStack.size() > returnCount)
							{
								valueStack.removeLast();
							}
							else
							{
								stateStack.addLast(States.DONE);
							}
							break;
						
						default:
							throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
					}
					
					break;
				
				case OBJECT:
					switch (token.getType())
					{
						case COMMA:
							stateStack.addLast(currentState);
							break;
						
						case DATUM:
							if (!(token.getValue() instanceof String))
							{
								throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
							}
							final String key = token.getValue();
							valueStack.addLast(key);
							stateStack.addLast(currentState);
							stateStack.addLast(States.ENTRY);
							break;
						
						case BRACE_R:
							if (valueStack.size() > returnCount)
							{
								valueStack.removeLast();
							}
							else
							{
								stateStack.addLast(States.DONE);
							}
							break;
						
						default:
							throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
					}
					break;
				
				case ENTRY:
					switch (token.getType())
					{
						case COLON:
							stateStack.addLast(currentState);
							break;
						
						case DATUM:
							String key = (String) valueStack.removeLast();
							SsonObject parent = (SsonObject) valueStack.getLast();
							parent.add(key, token.getValue());
							break;
						
						case BRACE_L:
							key = (String) valueStack.removeLast();
							parent = (SsonObject) valueStack.getLast();
							final SsonObject object = new SsonObject();
							parent.add(key, object);
							valueStack.addLast(object);
							stateStack.addLast(States.OBJECT);
							break;
						
						case BRACKET_L:
							key = (String) valueStack.removeLast();
							parent = (SsonObject) valueStack.getLast();
							final SsonArray array = new SsonArray();
							parent.add(key, array);
							valueStack.addLast(array);
							stateStack.addLast(States.ARRAY);
							break;
						
						default:
							throw new SsonException(lexer.getPosition(), SsonException.Problems.UNEXPECTED_TOKEN, token);
					}
					break;
				
				default:
					break;
			}
		}
		while ((!States.DONE.equals(currentState) || !Yytoken.Types.END.equals(token.getType())));
		
		if (valueStack.size() == 0)
		{
			return new SsonObject();
		}
		
		if (valueStack.size() == 1)
		{
			return new SsonObject(valueStack.get(0));
		}
		return new SsonArray(valueStack);
	}
	
	public static SsonObject deserialize(final String deserializable) throws SsonException
	{
		SsonObject returnable;
		StringReader readableDeserializable = null;
		
		try
		{
			readableDeserializable = new StringReader(deserializable);
			returnable = SsonSerialize.deserialize(readableDeserializable).getAsSsonObject();
		}
		catch (final NullPointerException caught)
		{
			returnable = null;
		}
		finally
		{
			
			if (readableDeserializable != null)
			{
				readableDeserializable.close();
			}
		}
		
		return returnable;
	}
	
	public static SsonArray deserialize(final String deserializable, final SsonArray defaultValue)
	{
		StringReader readable = null;
		SsonArray returnable;
		
		try
		{
			readable = new StringReader(deserializable);
			returnable = SsonSerialize.deserialize(readable).getAsSsonArray();
		}
		catch (NullPointerException | SsonException caught)
		{
			returnable = defaultValue;
		}
		finally
		{
			
			if (readable != null)
			{
				readable.close();
			}
		}
		return returnable;
	}
	
	public static SsonObject deserialize(final String deserializable, final SsonObject defaultValue)
	{
		StringReader readable = null;
		SsonObject returnable;
		
		try
		{
			readable = new StringReader(deserializable);
			returnable = SsonSerialize.deserialize(readable).getAsSsonObject();
		}
		catch (NullPointerException | SsonException caught)
		{
			returnable = defaultValue;
		}
		finally
		{
			
			if (readable != null)
			{
				readable.close();
			}
		}
		return returnable;
	}
	
	public static SsonArray deserializeMany(final Reader deserializable) throws SsonException
	{
		return SsonSerialize.deserialize(deserializable).getAsSsonArray();
	}
	
	public static String escape(final String escapable)
	{
		final StringBuilder builder = new StringBuilder();
		final int characters = escapable.length();
		
		for (int i = 0; i < characters; i++)
		{
			final char character = escapable.charAt(i);
			
			switch (character)
			{
				case '"':
					builder.append("\\\"");
					break;
				
				case '\\':
					builder.append("\\\\");
					break;
				
				case '\b':
					builder.append("\\b");
					break;
				
				case '\f':
					builder.append("\\f");
					break;
				
				case '\n':
					builder.append("\\n");
					break;
				
				case '\r':
					builder.append("\\r");
					break;
				
				case '\t':
					builder.append("\\t");
					break;
				
				case '/':
					builder.append("\\/");
					break;
				
				default:
					if (((character >= '\u0000') && (character <= '\u001F')) || ((character >= '\u007F') && (character <= '\u009F')) || ((character >= '\u2000') && (character <= '\u20FF')))
					{
						final String characterHexCode = Integer.toHexString(character);
						builder.append("\\u");
						
						for (int k = 0; k < (4 - characterHexCode.length()); k++)
						{
							builder.append("0");
						}
						builder.append(characterHexCode.toUpperCase());
					}
					else
					{
						builder.append(character);
					}
			}
		}
		
		return builder.toString();
	}
	
	public static boolean isNullValue(Object obj)
	{
		
		if (obj == null)
		{
			return true;
		}
		
		if (obj instanceof SsonElement)
		{
			
			if (obj instanceof SsonNull)
			{
				return true;
			}
			
			if (obj instanceof SsonPrimitive)
			{
				SsonPrimitive o = (SsonPrimitive) obj;
				
				try
				{
					
					if ((o.isString() || o.isCharacter()) && o.getAsString() == null)
					{
						return true;
					}
					
					if (o.isNumber())
					{
						
						if ((o.isByte() && o.getAsByte() == null) || (o.isShort() && o.getAsShort() == null))
						{
							return true;
						}
						else if (o.isInteger() && o.getAsInt() == null)
						{
							return true;
						}
						else if (o.isLong())
						{
							
							if (o.getAsLong() == null)
							{
								return true;
							}
						}
						else if (o.isFloat())
						{
							
							if (o.getAsFloat() == null || o.getAsDouble().isInfinite() || o.getAsDouble().isNaN())
							{
								return true;
							}
						}
						else if (o.isDouble() && (o.getAsDouble() == null || o.getAsDouble().isInfinite() || o.getAsDouble().isNaN()))
						{
							return true;
						}
					}
					else if ((o.isBoolean() && o.getAsBoolean() == null) || o.isSsonNull())
					{
						return true;
					}
				}
				catch (Exception e)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static Yytoken lexNextToken(final Yylex lexer) throws SsonException
	{
		Yytoken returnable;
		
		try
		{
			returnable = lexer.yylex();
		}
		catch (Exception caught)
		{
			caught.printStackTrace();
			throw new SsonException(-1, SsonException.Problems.UNKNOWN, caught);
		}
		
		if (returnable == null)
		{
			returnable = new Yytoken(Yytoken.Types.END, null);
		}
		
		return returnable;
	}
	
	private static States popNextState(final LinkedList<States> stateStack)
	{
		
		if (stateStack.size() > 0)
		{
			return stateStack.removeLast();
		}
		return States.ERROR;
	}
	
	public static void prettyPrint(final Reader readable, final Writer writable, final String indentation, final String newline) throws IOException, SsonException
	{
		final Yylex lexer = new Yylex(readable);
		Yytoken lexed;
		int level = 0;
		
		do
		{
			lexed = SsonSerialize.lexNextToken(lexer);
			
			switch (lexed.getType())
			{
				case COLON:
					writable.append(lexed.getValue().toString());
					break;
				
				case COMMA:
					writable.append(lexed.getValue().toString());
					writable.append(newline);
					for (int i = 0; i < level; i++)
					{
						writable.append(indentation);
					}
					break;
				
				case END:
					break;
				
				case BRACE_L:
				case BRACKET_L:
					writable.append(lexed.getValue().toString());
					writable.append(newline);
					level++;
					for (int i = 0; i < level; i++)
					{
						writable.append(indentation);
					}
					break;
				
				case BRACE_R:
				case BRACKET_R:
					writable.append(newline);
					level--;
					for (int i = 0; i < level; i++)
					{
						writable.append(indentation);
					}
					writable.append(lexed.getValue().toString());
					break;
				
				default:
					if (lexed.getValue() == null)
					{
						writable.append("null");
					}
					else if (lexed.getValue() instanceof String)
					{
						writable.append("\"");
						writable.append(SsonSerialize.escape(lexed.getValue()));
						writable.append("\"");
					}
					else
					{
						writable.append(lexed.getValue().toString());
					}
					break;
			}
		}
		
		while (!Yytoken.Types.END.equals(lexed.getType()));
		writable.flush();
	}
	
	public static String prettyPrint(final String printable)
	{
		final StringWriter writer = new StringWriter();
		
		try
		{
			SsonSerialize.prettyPrint(new StringReader(printable), writer, "\t", "\n");
		}
		catch (final IOException | SsonException caught)
		{
		}
		return writer.toString();
	}
	
	public static void serialize(final SsonElement sson, final StringBuilder writable) throws IOException
	{
		
		if (sson instanceof SsonNull || sson == null)
		{
			writable.append("null");
		}
		else if (sson instanceof SsonPrimitive)
		{
			SsonSerialize.serializePrimitive((SsonPrimitive) sson, writable);
		}
		else if (sson instanceof SsonArray)
		{
			boolean isFirstElement = true;
			writable.append("[");
			
			for (int i = 0; i < ((SsonArray) sson).size(); i++)
			{
				
				if (!isFirstElement)
				{
					writable.append(",");
				}
				
				SsonSerialize.serialize(((SsonArray) sson).get(i), writable);
				
				isFirstElement = false;
			}
			
			SsonSerialize.removeBeforeComma(writable);
			writable.append("]");
		}
		else if (sson instanceof SsonObject)
		{
			ModelSerializeMap modelSerializeMap = ((SsonObject) sson).getModelSerializeMap();
			boolean isSessionAdministrator = ((SsonObject) sson).isSessionAdministrator();
			String userUid = ((SsonObject) sson).getUserUidAttribute();
			String userUidInObject = "";
			boolean isFirstElement = true;
			boolean passObject = false;
			
			writable.append("{");
			
			for (Entry<String, SsonElement> elem : ((SsonObject) sson).entrySet())
			{
				FieldSerializeMap f = ((SsonObject) sson).getFieldSerializeMap(elem.getKey());
				
				if (f.isUserUidField() && elem.getValue().isSsonPrimitive())
				{
					userUidInObject = elem.getValue().getAsSsonPrimitive().getAsString();
				}
			}
			
			switch (modelSerializeMap.getExposeRule())
			{
				case HIDE:
					passObject = true;
					break;
				
				case NOT_USER_UID:
					if (!userUidInObject.isEmpty() && userUidInObject.equals(userUid))
					{
						passObject = true;
					}
					break;
				
				case ONLY_USER_UID:
					if (userUidInObject.isEmpty() || !userUidInObject.equals(userUid))
					{
						passObject = true;
					}
					break;
				
				case ONLY_ADMINISTRATOR:
					if (!isSessionAdministrator)
					{
						passObject = true;
					}
					break;
				
				default:
					break;
			}
			
			if (!passObject)
			{
				
				for (Entry<String, SsonElement> elem : ((SsonObject) sson).entrySet())
				{
					FieldSerializeMap fieldSerializeMap = ((SsonObject) sson).getFieldSerializeMap(elem.getKey());
					String fieldName = ((SsonObject) sson).getSerializeFieldName(elem.getKey());
					boolean passElement = false;
					
					switch (fieldSerializeMap.getExposeRule())
					{
						case HIDE:
							passElement = true;
							break;
						
						case ONLY_ADMINISTRATOR:
							if (!isSessionAdministrator)
							{
								passElement = true;
							}
							break;
						
						case NOT_USER_UID:
							if (!userUidInObject.isEmpty() && userUidInObject.equals(userUid))
							{
								passElement = true;
							}
							break;
						
						case ONLY_USER_UID:
							if (userUidInObject.isEmpty() || !userUidInObject.equals(userUid))
							{
								passElement = true;
							}
							break;
						
						default:
							break;
					}
					
					if ((fieldSerializeMap.isDisposeNull()) && SsonSerialize.isNullValue(elem.getValue()))
					{
						continue;
					}
					
					if (passElement)
					{
						continue;
					}
					
					if (!isFirstElement)
					{
						writable.append(",");
					}
					else
					{
						isFirstElement = false;
					}
					
					writable.append("\"" + SsonSerialize.escape(fieldName) + "\":");
					
					if (!SsonSerialize.serializeExtraObjectPrimitive(elem.getValue(), writable))
					{
						SsonSerialize.serialize(elem.getValue(), writable);
					}
				}
			}
			
			SsonSerialize.removeBeforeComma(writable);
			writable.append("}");
		}
	}
	
	public static boolean serializeExtraObjectPrimitive(SsonElement obj, final StringBuilder writable)
	{
		
		if (obj.getObject() instanceof Timestamp)
		{
			Timestamp timestamp = (Timestamp) obj.getObject();
			
			try
			{
				SsonSerialize.serialize(new SsonPrimitive(timestamp.getTime()), writable);
			}
			catch (IOException e)
			{
				writable.append("");
			}
			
			return true;
		}
		
		return false;
	}
	
	public static void serializePrimitive(SsonPrimitive sson, final StringBuilder writable) throws IOException
	{
		
		try
		{
			
			if (sson.isString() || sson.isCharacter())
			{
				writable.append('"');
				writable.append(SsonSerialize.escape(sson.getAsString()));
				writable.append('"');
			}
			else if (sson.isNumber())
			{
				
				if (sson.isByte())
				{
					writable.append(String.valueOf(sson.getAsByte()));
				}
				else if (sson.isShort())
				{
					writable.append(sson.getAsShort());
				}
				else if (sson.isInteger())
				{
					writable.append(sson.getAsInt());
				}
				else if (sson.isLong())
				{
					writable.append(sson.getAsLong());
				}
				else if (sson.isFloat())
				{
					
					if (sson.getAsDouble().isInfinite() || sson.getAsDouble().isNaN())
					{
						writable.append("null");
					}
					else
					{
						writable.append(sson.getAsFloat());
					}
				}
				else if (sson.isDouble())
				{
					
					if (sson.getAsDouble().isInfinite() || sson.getAsDouble().isNaN())
					{
						writable.append("null");
					}
					else
					{
						writable.append(sson.getAsDouble());
					}
				}
				else
				{
					writable.append(String.valueOf(sson.getAsNumber()));
				}
			}
			else if (sson.isBoolean())
			{
				writable.append(sson.getAsBoolean().toString());
			}
			else if (sson.isSsonNull())
			{
				writable.append("null");
			}
			else
			{
				writable.append(sson.value().toString());
			}
		}
		catch (Exception e)
		{
			writable.append("null");
		}
	}
	
	private SsonSerialize()
	{
	}
}
