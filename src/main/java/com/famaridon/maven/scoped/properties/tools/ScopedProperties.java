package com.famaridon.maven.scoped.properties.tools;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.Wrapper;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by famaridon on 10/07/2014.
 */
public class ScopedProperties
{

	// TODO : the user cant change this value with configuration.
	public static final int DEFAULT_THREAD_POOL_SIZE = 4;

	protected final ScopedPropertiesConfiguration configuration;
	protected final JAXBContext jaxbContext;
	protected final Set<Class<? extends ScopedPropertiesHandler>> handlerSet;

	public ScopedProperties(ScopedPropertiesConfiguration configuration)
	{
		// load any configuration.
		this.configuration = configuration;
		try
		{
			this.jaxbContext = JAXBContext.newInstance(Wrapper.class, Property.class);
		} catch (JAXBException e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
		// load all handlers
		Set<Class<? extends ScopedPropertiesHandler>> handlerSet = new HashSet<>();

		// load default handlers
		Set<Class<? extends ScopedPropertiesHandler>> foundClasses = getHandlers("com.famaridon.maven.scoped.properties.extension.defaults");
		handlerSet.addAll(foundClasses);

		// load custom handlers
		for (String packageName : configuration.getHandlerPackages())
		{
			Set<Class<? extends ScopedPropertiesHandler>> customFoundClasses = getHandlers("com.famaridon.maven.scoped.properties.extension.defaults");
			handlerSet.addAll(customFoundClasses);
		}

		this.handlerSet = Collections.unmodifiableSet(handlerSet);
	}

	protected final Set<Class<? extends ScopedPropertiesHandler>> getHandlers(String handlerPackage)
	{
		Reflections defaultHandlersReflections = new Reflections(handlerPackage);
		Set<Class<? extends ScopedPropertiesHandler>> foundClasses = defaultHandlersReflections.getSubTypesOf(ScopedPropertiesHandler.class);
		Iterator<Class<? extends ScopedPropertiesHandler>> iterator = foundClasses.iterator();
		while (iterator.hasNext())
		{
			Class<? extends ScopedPropertiesHandler> foundClass = iterator.next();
			if ( Modifier.isAbstract(foundClass.getModifiers()) )
			{
				iterator.remove();
			}
			try
			{
				Constructor<? extends ScopedPropertiesHandler> constructor = foundClass.getConstructor();
				if ( !Modifier.isPublic(constructor.getModifiers()) )
				{
					iterator.remove();
				}
			} catch (NoSuchMethodException e)
			{
				iterator.remove();
			}
		}
		return foundClasses;
	}

	public Set<File> buildPropertiesFiles() throws BuildPropertiesFilesException
	{
		File[] propertiesXmlFiles = configuration.getPropertiesXmlFolder().listFiles((FileFilter) new SuffixFileFilter(".properties.xml"));
		Set<File> outputFileSet = new HashSet<>(propertiesXmlFiles.length);
		Queue<Future<List<File>>> scopedPropertiesResults = new LinkedList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

		for (File propertiesXml : propertiesXmlFiles)
		{
			scopedPropertiesResults.offer(executorService.submit(new ScopedPropertiesThread(propertiesXml, this.jaxbContext, this.configuration, this.handlerSet)));
		}


		// after all thread started we get results.
		try
		{
			for (Future<List<File>> result = scopedPropertiesResults.poll(); result != null; result = scopedPropertiesResults.poll())
			{
				outputFileSet.addAll(result.get());
			}
		} catch (InterruptedException | ExecutionException e)
		{
			// if one fail kill other thread to fast stop.
			for (Future<List<File>> result : scopedPropertiesResults)
			{
				result.cancel(true);
			}
			throw new BuildPropertiesFilesException("One file can't be build !", e);
		}

		return outputFileSet;
	}
}
