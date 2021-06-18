package com.weinwork.util.sson.sson;

import java.io.IOException;
import java.io.Writer;

public interface SsonSerializable
{
	public String toJson();
	
	public void toJson(Writer writable) throws IOException;
}
