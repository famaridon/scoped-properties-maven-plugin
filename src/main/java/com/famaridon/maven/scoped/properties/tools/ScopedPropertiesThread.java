package com.famaridon.maven.scoped.properties.tools;

import com.famaridon.maven.scoped.properties.annotations.CustomHandler;
import com.famaridon.maven.scoped.properties.beans.FileDescriptor;
import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by famaridon on 11/08/2014.
 */
public class ScopedPropertiesThread implements Callable<List<File>> {
	private final File propertiesXml;
	private final Unmarshaller unmarshaller;
	private final ScopedPropertiesConfiguration configuration;
	protected Set<ScopedPropertiesHandler> handlerSet;
	protected Map<Class<?>, String> handlerNameMap;

	public ScopedPropertiesThread(File propertiesXml, JAXBContext jaxbContext, ScopedPropertiesConfiguration configuration, Set<Class<? extends ScopedPropertiesHandler>> handlerClassSet) throws BuildPropertiesFilesException {
		this.propertiesXml = propertiesXml;
		try {
			this.unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new BuildPropertiesFilesException("Unhandled JAXB exception : ", e);
		}
		this.configuration = configuration;
		this.handlerSet = new HashSet<>();
		this.handlerNameMap = new HashMap<>();
		for (Class<? extends ScopedPropertiesHandler> handler : handlerClassSet) {

			// read annotation's information
			CustomHandler customHandler = handler.getAnnotation(CustomHandler.class);
			String handelConfigurationKey;
			if (customHandler == null || StringUtils.isEmpty(customHandler.shortName())) {
				handelConfigurationKey = handler.getName();
			} else {
				handelConfigurationKey = customHandler.shortName();
			}
			handlerNameMap.put(handler, handelConfigurationKey);

			// create new instance for each handler.
			try {
				Constructor<? extends ScopedPropertiesHandler> constructor = handler.getConstructor();
				ScopedPropertiesHandler handlerInstance = constructor.newInstance();
				handlerSet.add(handlerInstance);
			} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
				throw new BuildPropertiesFilesException("Can't create handler instance. ", e);
			}
		}
	}

	@Override
	public List<File> call() throws BuildPropertiesFilesException {
		try {
			FileDescriptor fileDescriptor = (FileDescriptor) this.unmarshaller.unmarshal(propertiesXml);

			for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet) {
				scopedPropertiesHandler.startDocument(this.configuration, fileDescriptor.getHandlersConfiguration().get(handlerNameMap.get(scopedPropertiesHandler.getClass())), this.propertiesXml);
			}

			for (Property property : fileDescriptor.getItems()) {

				for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet) {
					Object propertyHandlerConfiguration = property.getHandlersConfiguration().get(handlerNameMap.get(scopedPropertiesHandler.getClass()));
					scopedPropertiesHandler.startProperty(property, propertyHandlerConfiguration);

				}
				for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet) {
					Object propertyHandlerConfiguration = property.getHandlersConfiguration().get(handlerNameMap.get(scopedPropertiesHandler.getClass()));
					scopedPropertiesHandler.endProperty(property, propertyHandlerConfiguration);

				}
			}
			List<File> outputFileList = new ArrayList<>(this.handlerSet.size());
			String baseName = FilenameUtils.getBaseName(propertiesXml.getName());
			for (ScopedPropertiesHandler scopedPropertiesHandler : handlerSet) {
				File outputFile = scopedPropertiesHandler.endDocument(baseName);
				if (outputFile != null) {
					outputFileList.add(outputFile);
				}
			}
			return outputFileList;
		} catch (JAXBException e) {
			throw new BuildPropertiesFilesException("can't read xml file : " + propertiesXml.getAbsolutePath(), e);
		}
	}
}
