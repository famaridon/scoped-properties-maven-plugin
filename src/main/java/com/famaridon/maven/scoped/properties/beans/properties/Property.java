package com.famaridon.maven.scoped.properties.beans.properties;

import com.famaridon.maven.scoped.properties.beans.adapter.EnvironmentAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * this is simple property bean
 * Created by famaridon on 07/07/2014.
 *
 * @author famaridon
 */
@XmlRootElement()
@XmlType(namespace = "http://famaridon.com/properties")
public class Property
{
	private String name;
	private String defaultValue;
	private Map<String, String> values = new HashMap<>();

	@XmlAttribute
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@XmlJavaTypeAdapter(EnvironmentAdapter.class)
	public Map<String, String> getValues()
	{
		return values;
	}

	public void setValues(Map<String, String> values)
	{
		this.values = values;
	}

	@XmlElement(name = "default-value")
	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
}
