package com.famaridon.maven.scoped.properties.extension.defaults.configuration.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by famaridon on 03/09/2014.
 */
@XmlRootElement
public class PropertiesRootConfiguration {

	protected String prefix;

	@XmlAttribute
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
