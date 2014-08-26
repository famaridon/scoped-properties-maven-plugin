package com.famaridon.maven.scoped.properties.test;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.tools.ScopedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */

public class MultiThreadScopedPropertiesTest {

	public static final String CUSTOM_PROPERTIES_XML_FILE_NAME = "custom.properties.xml";
	protected File tempDirectory;
	protected File tempDirectoryInput;
	protected File tempDirectoryOutput;
	protected int inputFileCount = 0;

	protected Properties properties = new Properties();

	@Before
	public void inti() throws IOException, BuildPropertiesFilesException {
		// build a temp directory using java nio

		tempDirectory = new File("./target/test/");
		tempDirectory.mkdirs();
		tempDirectoryInput = new File(tempDirectory, "input");
		tempDirectoryInput.mkdir();
		tempDirectoryOutput = new File(tempDirectory, "output");
		tempDirectoryOutput.mkdir();

		// copy the resource custom.properties.xml into user temps directory to test in real case
		// WARNING : if you copy other file all test should be updated

		for (int i = 0; i < 25; i++) {
			File propertiesXml = new File(tempDirectoryInput, "file" + i + ".properties.xml");
			try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesXml)) {
				IOUtils.copy(getClass().getClassLoader().getResourceAsStream("input/" + CUSTOM_PROPERTIES_XML_FILE_NAME), fileOutputStream);
				inputFileCount++;
			}
		}

		// run the command
		ScopedPropertiesConfiguration.Builder configurationBuilder = new ScopedPropertiesConfiguration.Builder();
		configurationBuilder.appendOutputFolder(tempDirectoryOutput);
		configurationBuilder.appendPropertiesXmlFolder(tempDirectoryInput);
		configurationBuilder.appendTargetScope(Scopes.PRODUCTION);
		ScopedProperties scopedProperties = new ScopedProperties(configurationBuilder.build());
		Set<File> outputFileSet = scopedProperties.buildPropertiesFiles();
	}

	/**
	 * test the production properties building process.
	 *
	 * @throws com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException
	 */
	@Test
	public void testFileCount() {
		Assert.assertTrue(tempDirectoryOutput.listFiles().length == inputFileCount);
	}

	@After
	public void clean() throws IOException {
		FileUtils.deleteDirectory(this.tempDirectory);
	}

}
