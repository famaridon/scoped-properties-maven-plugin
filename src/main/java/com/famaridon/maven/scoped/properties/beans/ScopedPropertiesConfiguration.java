package com.famaridon.maven.scoped.properties.beans;

import java.io.File;

/**
 * Created by famaridon on 07/08/2014.
 */
public class ScopedPropertiesConfiguration
{
	private final File propertiesXmlFolder;
	private final File outputFolder;
	private final String targetScope;

	private ScopedPropertiesConfiguration(Builder builder)
	{
		this.propertiesXmlFolder = builder.propertiesXmlFolder;
		this.outputFolder = builder.outputFolder;
		this.targetScope = builder.targetScope;
	}

	public File getPropertiesXmlFolder()
	{
		return propertiesXmlFolder;
	}

	public File getOutputFolder()
	{
		return outputFolder;
	}

	public String getTargetScope()
	{
		return targetScope;
	}

	public static class Builder
	{
		private File propertiesXmlFolder;
		private File outputFolder;
		private String targetScope;

		public Builder()
		{
		}

		public Builder appendPropertiesXmlFolder(File propertiesXmlFolder)
		{
			this.propertiesXmlFolder = propertiesXmlFolder;
			return this;
		}

		public Builder appendOutputFolder(File outputFolder)
		{
			this.outputFolder = outputFolder;
			return this;
		}

		public Builder appendTargetScope(String targetScope)
		{
			this.targetScope = targetScope;
			return this;
		}

		public ScopedPropertiesConfiguration build()
		{
			return new ScopedPropertiesConfiguration(this);
		}

	}
}
