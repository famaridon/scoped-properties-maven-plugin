/**
 *
 */
package com.famaridon.maven.scoped.properties.beans.adapter;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * the JAXB adapter to match XML binding
 *
 * @author famaridon
 */
@XmlType(namespace = "http://famaridon.com/properties")
public class EnvironmentAdapter extends XmlAdapter<Environment, Map<String, String>>
{

	@Override
	public Map<String, String> unmarshal(Environment adaptedMap) throws Exception
	{
		Map<String, String> map = new HashMap<>();
		for (Entry entry : adaptedMap.entry)
		{
			map.put(entry.key, entry.value);
		}
		return map;
	}

	@Override
	public Environment marshal(Map<String, String> map) throws Exception
	{
		Environment adaptedMap = new Environment();
		for (Map.Entry<String, String> mapEntry : map.entrySet())
		{
			Entry entry = new Entry();
			entry.key = mapEntry.getKey();
			entry.value = mapEntry.getValue();
			adaptedMap.entry.add(entry);
		}
		return adaptedMap;
	}

}
