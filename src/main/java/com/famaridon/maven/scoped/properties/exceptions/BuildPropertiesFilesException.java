package com.famaridon.maven.scoped.properties.exceptions;

/**
 * Created by famaridon on 10/07/2014.
 */
public class BuildPropertiesFilesException extends Exception
{
	public BuildPropertiesFilesException()
	{
		super();
	}

	public BuildPropertiesFilesException(String s)
	{
		super(s);
	}

	public BuildPropertiesFilesException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

	public BuildPropertiesFilesException(Throwable throwable)
	{
		super(throwable);
	}

	protected BuildPropertiesFilesException(String s, Throwable throwable, boolean b, boolean b2)
	{
		super(s, throwable, b, b2);
	}
}
