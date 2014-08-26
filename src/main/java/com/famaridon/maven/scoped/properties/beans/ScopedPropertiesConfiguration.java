package com.famaridon.maven.scoped.properties.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by famaridon on 07/08/2014.
 */
public class ScopedPropertiesConfiguration {
	private final File propertiesXmlFolder;
	private final File outputFolder;
	private final String targetScope;
	private final List<String> handlerPackages;

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
