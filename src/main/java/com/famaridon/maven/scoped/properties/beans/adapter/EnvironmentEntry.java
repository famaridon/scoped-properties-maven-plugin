package com.famaridon.maven.scoped.properties.beans.adapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by famaridon on 12/07/2014.
 */
@XmlType(namespace = "http://famaridon.com/properties")
public class EnvironmentEntry {
	@XmlAttribute(name = "scope", required = true)
	public String key;

	@XmlElement
	public String value;

}