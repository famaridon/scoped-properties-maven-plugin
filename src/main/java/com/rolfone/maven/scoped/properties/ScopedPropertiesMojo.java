package com.rolfone.maven.scoped.properties;

import com.rolfone.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.rolfone.maven.scoped.properties.utils.ScopedPropertiesUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

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

		try
		{
			ScopedPropertiesUtils.buildPropertiesFiles(this.getPropertiesXmlFolder(), this.getOutputFolder(), this.getTargetScope());
		} catch (BuildPropertiesFilesException e)
		{
			throw new MojoExecutionException("An exception occur see cause : ", e);
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
