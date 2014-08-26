package com.famaridon.maven.scoped.properties.tools;

import com.famaridon.maven.scoped.properties.beans.FileDescriptor;
import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
public class ScopedProperties {

	// TODO : the user cant change this value with configuration.
	public static final int DEFAULT_THREAD_POOL_SIZE = 4;
	public static final String DEFAULT_HANDLER_PACKAGE = "com.famaridon.maven.scoped.properties.extension.defaults";
	protected static final Logger LOG = LoggerFactory.getLogger(ScopedProperties.class);
	protected final ScopedPropertiesConfiguration configuration;
	protected final JAXBContext jaxbContext;
	protected final Set<Class<? extends ScopedPropertiesHandler>> handlerSet;

	public ScopedProperties(ScopedPropertiesConfiguration configuration) {
		// load any configuration.
		this.configuration = configuration;
		// load all handlers
		Set<Class<? extends ScopedPropertiesHandler>> handlerSet = new HashSet<>();
		Set<Class<?>> jaxbClassesToBeBound = new HashSet<>();

		// load default handlers
		Reflections defaultReflections = new Reflections(DEFAULT_HANDLER_PACKAGE);
		Set<Class<? extends ScopedPropertiesHandler>> foundClasses = getHandlers(defaultReflections);
		handlerSet.addAll(foundClasses);
		// add default jaxb classes to be bounds
		jaxbClassesToBeBound.add(FileDescriptor.class);
		jaxbClassesToBeBound.add(Property.class);

		// load custom handlers
		for (String packageName : configuration.getHandlerPackages()) {
			Reflections customReflections = new Reflections(packageName);
			handlerSet.addAll(getHandlers(customReflections));
			jaxbClassesToBeBound.addAll(getJaxbClasses(customReflections));
		}

		this.handlerSet = Collections.unmodifiableSet(handlerSet);

		try {
			this.jaxbContext = JAXBContext.newInstance(jaxbClassesToBeBound.toArray(new Class<?>[jaxbClassesToBeBound.size()]));
		} catch (JAXBException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	protected final Set<Class<? extends ScopedPropertiesHandler>> getHandlers(Reflections handlersReflections) {
		Set<Class<? extends ScopedPropertiesHandler>> foundClasses = handlersReflections.getSubTypesOf(ScopedPropertiesHandler.class);
		Iterator<Class<? extends ScopedPropertiesHandler>> iterator = foundClasses.iterator();
		while (iterator.hasNext()) {
			Class<? extends ScopedPropertiesHandler> foundClass = iterator.next();
			LOG.debug("Handler {} found.", foundClass.getName());

			if (Modifier.isAbstract(foundClass.getModifiers())) {
				iterator.remove();
			}
			try {
				Constructor<? extends ScopedPropertiesHandler> constructor = foundClass.getConstructor();
				if (!Modifier.isPublic(constructor.getModifiers())) {
					iterator.remove();
					LOG.warn("The handler {} is removed default constructor not public.", foundClass.getName());
				}
			} catch (NoSuchMethodException e) {
				iterator.remove();
				LOG.warn("The handler {} is removed no default constructor found.", foundClass.getName());
			}
		}
		return foundClasses;
	}

	protected final Set<Class<?>> getJaxbClasses(Reflections handlersReflections) {
		Set<Class<?>> foundClasses = new HashSet<>();
		foundClasses.addAll(handlersReflections.getTypesAnnotatedWith(XmlRootElement.class));
		foundClasses.addAll(handlersReflections.getTypesAnnotatedWith(XmlElementDecl.class));
		foundClasses.addAll(handlersReflections.getTypesAnnotatedWith(XmlType.class));
		return foundClasses;
	}

	public Set<File> buildPropertiesFiles() throws BuildPropertiesFilesException {
		File[] propertiesXmlFiles = configuration.getPropertiesXmlFolder().listFiles((FileFilter) new SuffixFileFilter(".properties.xml"));
		Set<File> outputFileSet = new HashSet<>(propertiesXmlFiles.length);
		Queue<Future<List<File>>> scopedPropertiesResults = new LinkedList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

		for (File propertiesXml : propertiesXmlFiles) {
			LOG.info("Run thread to build {} file.", propertiesXml.getName());
			scopedPropertiesResults.offer(executorService.submit(new ScopedPropertiesThread(propertiesXml, this.jaxbContext, this.configuration, this.handlerSet)));
		}

		// after all thread started we get results.
		try {
			for (Future<List<File>> result = scopedPropertiesResults.poll(); result != null; result = scopedPropertiesResults.poll()) {
				outputFileSet.addAll(result.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			// if one fail kill other thread to fast stop.
			for (Future<List<File>> result : scopedPropertiesResults) {
				result.cancel(true);
			}
			throw new BuildPropertiesFilesException("One handler fail stop others !", e);
		}

		return outputFileSet;
	}
}
