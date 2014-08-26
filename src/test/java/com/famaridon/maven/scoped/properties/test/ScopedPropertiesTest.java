package com.famaridon.maven.scoped.properties.test;

import com.famaridon.maven.scoped.properties.beans.ScopedPropertiesConfiguration;
import com.famaridon.maven.scoped.properties.exceptions.BuildPropertiesFilesException;
import com.famaridon.maven.scoped.properties.tools.ScopedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */

public class ScopedPropertiesTest {

	public static final String CUSTOM_PROPERTIES_XML_FILE_NAME = "custom.properties.xml";
	protected static File tempDirectory;
	protected static File tempDirectoryInput;
	protected static File tempDirectoryOutput;
	protected static File propertiesXml;
	protected static int inputFileCount = 0;

	protected static Properties properties = new Properties();

	@BeforeClass
	public static void inti() throws IOException, BuildPropertiesFilesException {
		// build a temp directory using java nio
		tempDirectory = new File("./target/test/");
		tempDirectory.mkdirs();
		tempDirectoryInput = new File(tempDirectory, "input");
		tempDirectoryInput.mkdir();
		tempDirectoryOutput = new File(tempDirectory, "output");
		tempDirectoryOutput.mkdir();

		// copy the resource custom.properties.xml into user temps directory to test in real case
		// WARNING : if you copy other file all test should be updated
		propertiesXml = new File(tempDirectoryInput, CUSTOM_PROPERTIES_XML_FILE_NAME);
		try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesXml)) {
			IOUtils.copy(ScopedPropertiesTest.class.getClassLoader().getResourceAsStream("input/" + CUSTOM_PROPERTIES_XML_FILE_NAME), fileOutputStream);
			inputFileCount++;
		}

		// run the command
		ScopedPropertiesConfiguration.Builder configurationBuilder = new ScopedPropertiesConfiguration.Builder();
		configurationBuilder.appendOutputFolder(tempDirectoryOutput);
		configurationBuilder.appendPropertiesXmlFolder(tempDirectoryInput);
		configurationBuilder.appendTargetScope(Scopes.PRODUCTION);
		ScopedProperties scopedProperties = new ScopedProperties(configurationBuilder.build());
		Set<File> outputFileSet = scopedProperties.buildPropertiesFiles();
		Assert.assertTrue(outputFileSet.size() == inputFileCount);

		// the output file name should be custom.properties
		// we can't compare file byte per byte because properties output the timestamp.
		File output = new File(tempDirectoryOutput, "custom.properties");
		try (FileInputStream inputStream = new FileInputStream(output)) {
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			Assert.fail("output file not found!");
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@AfterClass
	public static void clean() throws IOException {
		FileUtils.deleteDirectory(tempDirectory);
	}

	/**
	 * test the production properties building process.
	 *
	 * @throws BuildPropertiesFilesException
	 */
	@Test
	public void testSimpleProperty() {
		Assert.assertEquals("scoped-properties-maven-plugin", properties.getProperty("simple.property"));
	}

	@Test
	public void testUnicodeProperty() {
		Assert.assertEquals("ç Σ", properties.getProperty("property.value.with.unicode.char"));
	}

	@Test
	public void testDefaultProperty() {
		Assert.assertEquals("a default value", properties.getProperty("property.with.default.value"));
	}

	@Test
	public void testEscapeCharProperty() {
		Assert.assertEquals("https://git.famaridon.com?tab=repositories", properties.getProperty("property.with.equals"));
		Assert.assertEquals("https://git.famaridon.com?tab=repositories", properties.getProperty("property.key with space"));
	}

}
