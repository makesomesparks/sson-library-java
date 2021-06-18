package com.weinwork.util.sson.ylex;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Yytoken
{
	public enum Types
	{
		BRACE_L, BRACKET_L, 
		BRACE_R, BRACKET_R, 
		COLON, COMMA, DATUM, 
		END
	}
	
	private final Types type;
	private final String value;
	
	public Yytoken(final Types type, final Object value)
	{
		this.type = type;
		
		switch (type)
		{
			case COLON:
				this.value = ":";
				break;
			
			case COMMA:
				this.value = ",";
				break;
			
			case END:
				this.value = "";
				break;
			
			case BRACE_L:
				this.value = "{";
				break;
			
			case BRACE_R:
				this.value = "}";
				break;
			
			case BRACKET_L:
				this.value = "[";
				break;
			
			case BRACKET_R:
				this.value = "]";
				break;
			
			default:
				if (value != null)
				{
					this.value = value.toString();
				}
				else
				{
					this.value = null;
				}
				
				break;
		}
	}
}
