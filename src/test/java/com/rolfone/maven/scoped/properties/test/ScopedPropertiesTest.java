package com.rolfone.maven.scoped.properties.test;

import com.rolfone.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.rolfone.maven.scoped.properties.utils.ScopedPropertiesUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */

public class ScopedPropertiesTest
{

	public static final String CUSTOM_PROPERTIES_XML_FILE_NAME = "custom.properties.xml";
	protected File tempDirectory;
	protected File tempDirectoryInput;
	protected File tempDirectoryOutput;
	protected File propertiesXml;
	protected int inputFileCount = 0;

	@Before
	public void inti() throws IOException
	{
		// build a temp directory using java nio
		Path tempDirectoryPath = Files.createTempDirectory("ScopedPropertiesTest");

		tempDirectory = tempDirectoryPath.toFile();
		tempDirectoryInput = new File(tempDirectory, "input");
		tempDirectoryInput.mkdirs();
		tempDirectoryOutput = new File(tempDirectory, "output");
		tempDirectoryOutput.mkdirs();

		// copy the resource custom.properties.xml into user temps directory to test in real case
		// WARNING : if you copy other file all test should be updated
		propertiesXml = new File(tempDirectoryInput, CUSTOM_PROPERTIES_XML_FILE_NAME);
		try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesXml);)
		{
			IOUtils.copy(getClass().getClassLoader().getResourceAsStream(CUSTOM_PROPERTIES_XML_FILE_NAME), fileOutputStream);
			inputFileCount++;
		}
	}

	/**
	 * test the production properties building process.
	 *
	 * @throws BuildPropertiesFilesException
	 */
	@Test
	public void buildFileProperties() throws BuildPropertiesFilesException
	{
		// run the command
		Set<File> outputFileSet = ScopedPropertiesUtils.buildPropertiesFiles(tempDirectoryInput, tempDirectoryOutput, SCOPE.PRODUCTION);
		Assert.assertTrue(outputFileSet.size() == inputFileCount);

		System.out.println(outputFileSet);
		// the output file name should be

	}

	@Test
	public void buildFilePropertiesWithDefault() throws BuildPropertiesFilesException
	{

	}

	@After
	public void clean() throws IOException
	{

	}

	public interface SCOPE
	{
		public static final String PRODUCTION = "production";
		public static final String DEVELOPMENT = "development";
		public static final String PRE_PRODUCTION = "pre-production";
	}

}
