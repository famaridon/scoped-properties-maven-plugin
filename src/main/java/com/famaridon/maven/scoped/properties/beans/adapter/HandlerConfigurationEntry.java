package com.famaridon.maven.scoped.properties.beans.adapter;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by famaridon on 12/07/2014.
 */
@XmlType(namespace = "http://famaridon.com/properties")
public class HandlerConfigurationEntry {
	@XmlAttribute(name = "handler", required = true)
	public String handler;

	@XmlAnyElement(lax = true)
	public Object configuration;

}