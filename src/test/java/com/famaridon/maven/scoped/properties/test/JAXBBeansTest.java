package com.famaridon.maven.scoped.properties.test;

import com.famaridon.maven.scoped.properties.beans.FileDescriptor;
import com.famaridon.maven.scoped.properties.beans.properties.Property;
import com.famaridon.maven.scoped.properties.test.beans.CustomHandlerConfiguration;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by famaridon on 19/05/2014.
 *
 * @author famaridon
 */

public class JAXBBeansTest {


	@Test
	public void testSimpleProperty() throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(FileDescriptor.class, CustomHandlerConfiguration.class);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


		FileDescriptor fileDescriptor = new FileDescriptor();

		Property property = new Property();
		property.setName("name");

		fileDescriptor.getItems().add(property);
		CustomHandlerConfiguration configuration = new CustomHandlerConfiguration();
		configuration.value = "An Object value";
		fileDescriptor.getHandlersConfiguration().put("a.class.Name", configuration);

		marshaller.marshal(fileDescriptor, System.out);


		ByteArrayOutputStream out = new ByteArrayOutputStream();
		marshaller.marshal(fileDescriptor, out);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		FileDescriptor parsed = (FileDescriptor) unmarshaller.unmarshal(new ByteArrayInputStream(out.toByteArray()));

		System.out.println(parsed.getHandlersConfiguration());

	}


}
