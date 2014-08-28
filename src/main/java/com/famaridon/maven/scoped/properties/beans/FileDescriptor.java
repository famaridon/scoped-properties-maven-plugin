package com.famaridon.maven.scoped.properties.beans;

import com.famaridon.maven.scoped.properties.beans.adapter.HandlerConfigurationAdapter;
import com.famaridon.maven.scoped.properties.beans.properties.Property;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the JAXB bean to parse .properties.xml files.
 * Created by famaridon on 07/07/2014.
 *
 * @author famaridon
 */
@XmlRootElement(name = "properties")
public class FileDescriptor {

	/**
	 * A list of property description
	 */
	private List<Property> items;
	/**
	 * custom handler configuration. The should be the handler full qualified class name.
	 */
	private Map<String, Object> handlersConfiguration;

	public FileDescriptor() {
		this.items = new ArrayList<>();
		this.handlersConfiguration = new HashMap<>();
	}

	public FileDescriptor(List<Property> items) {
		this.items = items;
	}

	@XmlElement(name = "property")
	public List<Property> getItems() {
		return items;
	}

	@XmlJavaTypeAdapter(HandlerConfigurationAdapter.class)
	public Map<String, Object> getHandlersConfiguration() {
		return handlersConfiguration;
	}

	public void setHandlersConfiguration(Map<String, Object> handlersConfiguration) {
		this.handlersConfiguration = handlersConfiguration;
	}

	public void setItems(List<Property> items) {
		this.items = items;
	}
}