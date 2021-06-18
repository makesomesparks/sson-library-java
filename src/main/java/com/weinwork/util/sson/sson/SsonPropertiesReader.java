package com.weinwork.util.sson.sson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.core.io.ClassPathResource;

public class SsonPropertiesReader
{
	public enum PropertyName
	{
		UserUidAttribute, AdministratorAttribute
	}
	
	static final public String PROFILE_FILENAME = "sson.properties";
	
	public static String get(final PropertyName propName) throws IOException
	{
		return SsonPropertiesReader.get(propName.toString());
	}
	
	public static String get(String propName) throws IOException
	{
		
		try
		{
			FileInputStream file = new FileInputStream(new ClassPathResource(SsonPropertiesReader.PROFILE_FILENAME).getFile().toPath().toFile());
			Properties props = new Properties();
			String prop;
			
			props.load(file);
			prop = props.getProperty(propName);
			
			file.close();
			
			if (prop == null)
			{
				return "";
			}
			
			return prop;
			
		}
		catch (IOException e)
		{
			throw new IOException("Could not read " + ClassPath.getClassPath() + "/" + SsonPropertiesReader.PROFILE_FILENAME);
		}
	}
}
