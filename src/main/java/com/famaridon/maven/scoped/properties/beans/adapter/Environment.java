package com.famaridon.maven.scoped.properties.beans.adapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by famaridon on 12/07/2014.
 */
@XmlType(namespace = "http://famaridon.com/properties")
public class Environment
{
	@XmlElement(name = "environment", required = true)
	public List<Entry> entry = new ArrayList<>();

}
