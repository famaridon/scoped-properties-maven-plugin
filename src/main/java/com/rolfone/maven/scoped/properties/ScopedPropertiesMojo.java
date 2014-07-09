package com.rolfone.maven.scoped.properties;

import com.rolfone.maven.scoped.properties.beans.Wrapper;
import com.rolfone.maven.scoped.properties.beans.properties.Property;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */
@Mojo(name = "build-properties-files", threadSafe = false, requiresProject = false, requiresDirectInvocation = true)
public class ScopedPropertiesMojo extends AbstractMojo
{

	/**
	 * specify the input XML descriptor folder
	 */
	@Parameter(property = "propertiesXmlFolder", required = true)
	protected File propertiesXmlFolder;

	/**
	 * specify the .properties file output folder
	 */
	@Parameter(property = "outputFolder", required = true)
	protected File outputFolder;

	/**
	 * the XML descriptor's scope used to build .properties files
	 */
	@Parameter(property = "targetScope", required = true)
	protected String targetScope;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{

		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;
		try
		{
			jaxbContext = JAXBContext.newInstance(Wrapper.class, Property.class);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e)
		{
			throw new MojoExecutionException(e.getMessage());
		}


		File[] propertiesXmlFiles = propertiesXmlFolder.listFiles((FileFilter) new SuffixFileFilter(".properties.xml"));
		for (File proprtiesXml : propertiesXmlFiles)
		{
			try
			{
				Wrapper<Property> wrapper = (Wrapper<Property>) unmarshaller.unmarshal(proprtiesXml);

				Properties properties = new Properties();
				for (Property property : wrapper.getItems())
				{
					properties.setProperty(property.getName(), property.getValues().get(targetScope));
				}

				try (FileWriter writer = new FileWriter(new File(outputFolder, FilenameUtils.getBaseName(proprtiesXml.getName()))))
				{
					properties.store(writer, "Maven plugin building file for scope : " + targetScope);
				} catch (IOException e)
				{
					throw new MojoFailureException("can't write properties file", e);
				}


			} catch (JAXBException e)
			{
				throw new MojoFailureException("can't read xml file : " + proprtiesXml.getAbsolutePath(), e);
			}

		}


	}

	public File getPropertiesXmlFolder()
	{
		return propertiesXmlFolder;
	}

	public void setPropertiesXmlFolder(File propertiesXmlFolder)
	{
		this.propertiesXmlFolder = propertiesXmlFolder;
	}

	public File getOutputFolder()
	{
		return outputFolder;
	}

	public void setOutputFolder(File outputFolder)
	{
		this.outputFolder = outputFolder;
	}

	public String getTargetScope()
	{
		return targetScope;
	}

	public void setTargetScope(String targetScope)
	{
		this.targetScope = targetScope;
	}
}
