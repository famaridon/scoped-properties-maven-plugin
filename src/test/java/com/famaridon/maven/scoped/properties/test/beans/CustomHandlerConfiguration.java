package com.famaridon.maven.scoped.properties.test.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by famaridon on 20/08/2014.
 */
@XmlRootElement
public class CustomHandlerConfiguration {
	@XmlElement
	public String value;

}
