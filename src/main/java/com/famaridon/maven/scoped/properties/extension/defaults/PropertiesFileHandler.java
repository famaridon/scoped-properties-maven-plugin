package com.famaridon.maven.scoped.properties.extension.defaults;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by famaridon on 13/08/2014.
 */
public class PropertiesFileHandler implements ScopedPropertiesHandler
{

	private Properties properties;
	private ScopedPropertiesConfiguration configuration;
	private File currentFile;

	/**
	 * call at the begin of each .properties.xml parsing
	 *
	 * @param configuration the scoped properties input configuration.
	 * @param currentFile   the current parsing .properties.xml file
	 */
	@Override
	public void startDocument(ScopedPropertiesConfiguration configuration, File currentFile)
	{
		this.properties = new Properties();
		this.configuration = configuration;
		this.currentFile = currentFile;
	}

	/**
	 * call at any property add
	 */
	@Override
	public void startProperty(Property property)
	{
		String value = property.getValues().get(configuration.getTargetScope());
		if ( StringUtils.isEmpty(value) )
		{
			value = property.getDefaultValue();
		}
		properties.setProperty(property.getName(), value);
	}

	/**
	 * call after any property add
	 */
	@Override
	public void endProperty(Property property)
	{

	}

	/**
	 * call at the end of each .properties.xml parsing
	 */
	@Override
	public File endDocument(String fileBaseName) throws BuildPropertiesFilesException
	{
		File outputFile = new File(configuration.getOutputFolder(), fileBaseName);
		// never use a writer with stream unicode char is encoded.
		try (FileOutputStream outputStream = new FileOutputStream(outputFile))
		{
			properties.store(outputStream, "Maven plugin building file for scope : " + configuration.getTargetScope());
		} catch (IOException e)
		{
			throw new BuildPropertiesFilesException("can't write properties file", e);
		}
		return outputFile;
	}
}
