package com.famaridon.maven.scoped.properties.utils;

import com.famaridon.maven.scoped.properties.beans.Wrapper;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by famaridon on 10/07/2014.
 */
public class ScopedPropertiesUtils
{

	private ScopedPropertiesUtils()
	{
	}

	public static final Set<File> buildPropertiesFiles(File propertiesXmlFolder, File outputFolder, String targetScope) throws BuildPropertiesFilesException
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
		Set<File> outputFileSet = new HashSet<>(propertiesXmlFiles.length);
		for (File propertiesXml : propertiesXmlFiles)
		{
			try (FileInputStream inputStream = new FileInputStream(propertiesXml);
				 Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));)
			{
				Wrapper<Property> wrapper = (Wrapper<Property>) unmarshaller.unmarshal(propertiesXml);

				Properties properties = new Properties();
				for (Property property : wrapper.getItems())
				{
					String value = property.getValues().get(targetScope);
					if ( StringUtils.isEmpty(value) )
					{
						value = property.getDefaultValue();
					}
					properties.setProperty(property.getName(), value);
				}

				File outputFile = new File(outputFolder, FilenameUtils.getBaseName(propertiesXml.getName()));
				// never use a writer with stream unicode char is encoded.
				try (FileOutputStream outputStream = new FileOutputStream(outputFile))
				{
					properties.store(outputStream, "Maven plugin building file for scope : " + targetScope);
				} catch (IOException e)
				{
					throw new BuildPropertiesFilesException("can't write properties file", e);
				}

				outputFileSet.add(outputFile);

			} catch (JAXBException | IOException e)
			{
				throw new BuildPropertiesFilesException("can't read xml file : " + propertiesXml.getAbsolutePath(), e);
			}

		}
		return outputFileSet;
	}

}
