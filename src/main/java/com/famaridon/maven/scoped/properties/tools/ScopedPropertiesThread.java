package com.famaridon.maven.scoped.properties.tools;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.Wrapper;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import org.apache.commons.io.FilenameUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by famaridon on 11/08/2014.
 */
public class ScopedPropertiesThread implements Callable<List<File>>
{
	private final File propertiesXml;
	private final Unmarshaller unmarshaller;
	private final ScopedPropertiesConfiguration configuration;
	protected Set<ScopedPropertiesHandler> handlerSet;

	public ScopedPropertiesThread(File propertiesXml, JAXBContext jaxbContext, ScopedPropertiesConfiguration configuration, Set<Class<? extends ScopedPropertiesHandler>> handlerClassSet) throws BuildPropertiesFilesException
	{
		this.propertiesXml = propertiesXml;
		try
		{
			this.unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e)
		{
			throw new BuildPropertiesFilesException("Unhandled JAXB exception : ", e);
		}
		this.configuration = configuration;
		this.handlerSet = new HashSet<>();
		for (Class<? extends ScopedPropertiesHandler> handler : handlerClassSet)
		{
			try
			{
				Constructor<? extends ScopedPropertiesHandler> constructor = handler.getConstructor();
				ScopedPropertiesHandler handlerInstance = constructor.newInstance();
				handlerSet.add(handlerInstance);
			} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
			{
				throw new BuildPropertiesFilesException("Can't create handler instance. ", e);
			}
		}
	}

	@Override
	public List<File> call() throws BuildPropertiesFilesException
	{
		try (FileInputStream inputStream = new FileInputStream(this.propertiesXml);
			 Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));)
		{
			Wrapper<Property> wrapper = (Wrapper<Property>) this.unmarshaller.unmarshal(propertiesXml);

			for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet)
			{
				scopedPropertiesHandler.startDocument(this.configuration, this.propertiesXml);
			}


			Properties properties = new Properties();
			for (Property property : wrapper.getItems())
			{
				for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet)
				{
					scopedPropertiesHandler.startProperty(property);

				}
				for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet)
				{
					scopedPropertiesHandler.endProperty(property);

				}
			}
			List<File> outputFileList = new ArrayList<>(this.handlerSet.size());
			String baseName = FilenameUtils.getBaseName(propertiesXml.getName());
			for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet)
			{
				File outputFile = scopedPropertiesHandler.endDocument(baseName);
				if ( outputFile != null )
				{
					outputFileList.add(outputFile);
				}
			}
			return outputFileList;
		} catch (JAXBException | IOException e)
		{
			throw new BuildPropertiesFilesException("can't read xml file : " + propertiesXml.getAbsolutePath(), e);
		}
	}
}
