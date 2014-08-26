package com.famaridon.maven.scoped.properties.beans.adapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by famaridon on 20/08/2014.
 */
@XmlType(namespace = "http://famaridon.com/properties")
public class HandlerConfiguration {
	@XmlElement(name = "configuration", required = true)
	public List<HandlerConfigurationEntry> handlerConfigurationEntries = new ArrayList<>();
}
