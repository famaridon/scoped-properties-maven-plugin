package com.famaridon.maven.scoped.properties.beans;

import com.famaridon.maven.scoped.properties.annotations.CustomHandler;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.extension.interfaces.ScopedPropertiesHandler;
import com.famaridon.maven.scoped.properties.tools.ScopedPropertiesThread;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Created by famaridon on 07/08/2014.
 */
public class ScopedPropertiesConfiguration implements Cloneable {

	private final File propertiesXmlFolder;
	private final File outputFolder;
	private final String targetScope;
	private final List<String> handlerPackages;

	private transient Map<Class<?>, String> handlerNameMap;
	private transient Set<Class<? extends ScopedPropertiesHandler>> handlerClassSet;
	private transient ScopedPropertiesThread currentThread;

	private ScopedPropertiesConfiguration(Builder builder) {
		this.propertiesXmlFolder = builder.propertiesXmlFolder;
		this.outputFolder = builder.outputFolder;
		this.targetScope = builder.targetScope;
		this.handlerPackages = builder.handlerPackages;
	}

	public File getPropertiesXmlFolder() {
		return propertiesXmlFolder;
	}

	public File getOutputFolder() {
		return outputFolder;
	}

	public String getTargetScope() {
		return targetScope;
	}

	public List<String> getHandlerPackages() {
		return handlerPackages;
	}

	public Set<Class<? extends ScopedPropertiesHandler>> getHandlerClassSet() {
		return handlerClassSet;
	}

	public void setHandlerClassSet(Set<Class<? extends ScopedPropertiesHandler>> handlerClassSet) {
		this.handlerClassSet = handlerClassSet;
		handlerNameMap = new HashMap<>();
		for (Class<? extends ScopedPropertiesHandler> handler : this.handlerClassSet) {
			// read annotation's information
			CustomHandler customHandler = handler.getAnnotation(CustomHandler.class);
			String handelConfigurationKey;
			if (customHandler == null || StringUtils.isEmpty(customHandler.shortName())) {
				handelConfigurationKey = handler.getName();
			} else {
				handelConfigurationKey = customHandler.shortName();
			}
			handlerNameMap.put(handler, handelConfigurationKey);
		}
	}

	public void setCurrentThread(ScopedPropertiesThread currentThread) {
		this.currentThread = currentThread;
	}

	public <T> T getHandlersConfiguration(Class<?> handler) {
		if (this.currentThread == null || this.currentThread.getFileDescriptor() == null) {
			throw new IllegalStateException("This method is called too early!");
		}
		return (T) this.currentThread.getFileDescriptor().getHandlersConfiguration().get(handlerNameMap.get(handler));
	}

	public <T> T getPropertyConfiguration(Property property, Class<?> handler) {
		if (this.currentThread == null || this.currentThread.getFileDescriptor() == null) {
			throw new IllegalStateException("This method is called too early!");
		}
		return (T) property.getHandlersConfiguration().get(handlerNameMap.get(handler));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ScopedPropertiesConfiguration)) return false;

		ScopedPropertiesConfiguration that = (ScopedPropertiesConfiguration) o;

		if (!handlerPackages.equals(that.handlerPackages)) return false;
		if (!outputFolder.equals(that.outputFolder)) return false;
		if (!propertiesXmlFolder.equals(that.propertiesXmlFolder)) return false;
		if (!targetScope.equals(that.targetScope)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = propertiesXmlFolder.hashCode();
		result = 31 * result + outputFolder.hashCode();
		result = 31 * result + targetScope.hashCode();
		result = 31 * result + handlerPackages.hashCode();
		return result;
	}

	@Override
	public ScopedPropertiesConfiguration clone() {
		ScopedPropertiesConfiguration o = null;
		try {
			o = (ScopedPropertiesConfiguration) super.clone();

			//  we must not clone it but gat the same instance.
			o.handlerNameMap = this.handlerNameMap;
			o.handlerClassSet = this.handlerClassSet;
		} catch (CloneNotSupportedException e) {
			// Ne devrait jamais arriver car nous implémentons l'interface Cloneable
			throw new IllegalStateException(e);
		}
		// on renvoie le clone
		return o;
	}

	public static class Builder {
		private File propertiesXmlFolder;
		private File outputFolder;
		private String targetScope;
		private List<String> handlerPackages = new ArrayList<>();

		public Builder() {
		}

		public Builder appendPropertiesXmlFolder(File propertiesXmlFolder) {
			this.propertiesXmlFolder = propertiesXmlFolder;
			return this;
		}

		public Builder appendOutputFolder(File outputFolder) {
			this.outputFolder = outputFolder;
			return this;
		}

		public Builder appendTargetScope(String targetScope) {
			this.targetScope = targetScope;
			return this;
		}

		public Builder appendHandlerPackages(List<String> handlerPackages) {
			this.handlerPackages = handlerPackages;
			return this;
		}

		public ScopedPropertiesConfiguration build() {
			return new ScopedPropertiesConfiguration(this);
		}

	}
}
