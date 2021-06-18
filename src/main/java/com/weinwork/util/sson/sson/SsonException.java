package com.weinwork.util.sson.sson;

public class SsonException extends Exception
{
	private static final long serialVersionUID = -6689804783632564385L;
	
	public enum Problems
	{
		UNEXPECTED_CHARACTER, 
		UNEXPECTED_TOKEN, 
		NOT_SUPPORT_OBJECT,
		UNKNOWN
	}
	
	private final int position;
	private final Problems problemType;
	private final Object unexpectedObject;
	
	public SsonException(final int position, final Problems problemType, final Object unexpectedObject)
	{
		this.position = position;
		this.problemType = problemType;
		this.unexpectedObject = unexpectedObject;
	}
	
	@Override
	public String getMessage()
	{
		final StringBuilder sb = new StringBuilder();
		
		switch (this.problemType)
		{
			
			case UNEXPECTED_CHARACTER:
				sb.append("The unexpected character (").append(this.unexpectedObject).append(") was found at position ").append(this.position).append(". Fix the parsable string and try again.");
				break;
			
			case UNEXPECTED_TOKEN:
				sb.append("The unexpected token ").append(this.unexpectedObject).append(" was found at position ").append(this.position).append(". Fix the parsable string and try again.");
				break;
			
			case UNKNOWN:
				sb.append("Please report this to the library's maintainer. The unexpected exception that should be addressed before trying again occurred at position ").append(this.position).append(":\n").append(this.unexpectedObject);
				break;
			
			case NOT_SUPPORT_OBJECT:
				sb.append("This object(" + (this.unexpectedObject.getClass().getName()) + ") is not support type");
				break;
			
			default:
				sb.append("Please report this to the library's maintainer. An error at position ").append(this.position).append(" occurred. There are no recovery recommendations available.");
				break;
		}
		
		return sb.toString();
	}
	
	public int getPosition()
	{
		return this.position;
	}
	
	public Problems getProblemType()
	{
		return this.problemType;
	}
	
	public Object getUnexpectedObject()
	{
		return this.unexpectedObject;
	}
}
