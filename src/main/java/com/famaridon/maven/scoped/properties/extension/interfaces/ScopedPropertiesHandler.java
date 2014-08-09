package com.famaridon.maven.scoped.properties.extension.interfaces;

/**
 * Created by famaridon on 09/08/2014.
 */
public interface ScopedPropertiesHandler
{

	/**
	 * call at the begin of each .properties.xml parssing
	 */
	public void startDocument();

	public void startProperty();

	public void endProperty();

	/**
	 * call at the end of each .properties.xml parssing
	 */
	public void endDocument();

}
