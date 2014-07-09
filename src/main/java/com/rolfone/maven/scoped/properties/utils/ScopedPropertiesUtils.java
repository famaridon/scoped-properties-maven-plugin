package com.rolfone.maven.scoped.properties.utils;

import com.rolfone.maven.scoped.properties.beans.Wrapper;
import com.rolfone.maven.scoped.properties.beans.properties.Property;
import com.rolfone.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by famaridon on 10/07/2014.
 */
public class ScopedPropertiesUtils
{

	private ScopedPropertiesUtils()
	{
	}

	public static final void buildPropertiesFiles(File propertiesXmlFolder, File outputFolder, String targetScope) throws BuildPropertiesFilesException
	{

		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;
		try
		{
			jaxbContext = JAXBContext.newInstance(Wrapper.class, Property.class);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e)
		{
			throw new BuildPropertiesFilesException(e.getMessage(), e);
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
					throw new BuildPropertiesFilesException("can't write properties file", e);
				}


			} catch (JAXBException e)
			{
				throw new BuildPropertiesFilesException("can't read xml file : " + proprtiesXml.getAbsolutePath(), e);
			}

		}


	}

}
