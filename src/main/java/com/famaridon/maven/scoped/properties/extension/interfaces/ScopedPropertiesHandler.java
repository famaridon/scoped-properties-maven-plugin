package com.famaridon.maven.scoped.properties.extension.interfaces;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;

import java.io.File;

/**
 * This interface define the Scoped properties handler.
 * Created by famaridon on 09/08/2014.
 */
public interface ScopedPropertiesHandler<F, P> {

	/**
	 * call at the begin of each .properties.xml parsing
	 *
	 * @param configuration the scoped properties input configuration.
	 * @param currentFile   the current parsing .properties.xml file
	 */
	public void startDocument(ScopedPropertiesConfiguration configuration, F fileHandlerConfiguration, File currentFile) throws BuildPropertiesFilesException;

	/**
	 * call at any property add
	 */
	public void startProperty(Property property, P propertyHandlerConfiguration) throws BuildPropertiesFilesException;

	/**
	 * call after any property add
	 */
	public void endProperty(Property property, P propertyHandlerConfiguration) throws BuildPropertiesFilesException;

	/**
	 * call at the end of each .properties.xml parsing
	 */
	public File endDocument(String fileBaseName) throws BuildPropertiesFilesException;

}
