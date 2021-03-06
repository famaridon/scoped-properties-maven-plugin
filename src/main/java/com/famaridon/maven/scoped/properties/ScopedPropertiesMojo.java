package com.famaridon.maven.scoped.properties;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.tools.ScopedProperties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.File;
import java.util.List;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */
@Mojo(name = "build-properties-files", threadSafe = false, requiresProject = false, requiresDirectInvocation = true)
public class ScopedPropertiesMojo extends AbstractMojo {

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

	/**
	 * all packages to scan for custom handlers
	 */
	@Parameter(property = "handlerPackages", required = false)
	protected List<String> handlerPackages;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// bind slf4j to maven log
		StaticLoggerBinder.getSingleton().setLog(getLog());
		try {
			ScopedPropertiesConfiguration.Builder configurationBuilder = new ScopedPropertiesConfiguration.Builder();
			configurationBuilder.appendOutputFolder(this.getOutputFolder());
			configurationBuilder.appendPropertiesXmlFolder(this.getPropertiesXmlFolder());
			configurationBuilder.appendTargetScope(this.getTargetScope());
			configurationBuilder.appendHandlerPackages(this.getHandlerPackages());
			ScopedProperties scopedProperties = new ScopedProperties(configurationBuilder.build());
			scopedProperties.buildPropertiesFiles();
		} catch (BuildPropertiesFilesException e) {
			throw new MojoExecutionException("An exception occur see cause :  ", e);
		}

	}

	public File getPropertiesXmlFolder() {
		return propertiesXmlFolder;
	}

	public void setPropertiesXmlFolder(File propertiesXmlFolder) {
		this.propertiesXmlFolder = propertiesXmlFolder;
	}

	public File getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getTargetScope() {
		return targetScope;
	}

	public void setTargetScope(String targetScope) {
		this.targetScope = targetScope;
	}

	public List<String> getHandlerPackages() {
		return handlerPackages;
	}

	public void setHandlerPackages(List<String> handlerPackages) {
		this.handlerPackages = handlerPackages;
	}
}
