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
public class EnvironmentAdapter extends XmlAdapter<Environment, Map<String, String>> {

	@Override
	public Map<String, String> unmarshal(Environment adaptedMap) throws Exception {
		Map<String, String> map = new HashMap<>();
		for (EnvironmentEntry environmentEntry : adaptedMap.environmentEntry) {
			map.put(environmentEntry.key, environmentEntry.value);
		}
		return map;
	}

	@Override
	public Environment marshal(Map<String, String> map) throws Exception {
		Environment adaptedMap = new Environment();
		for (Map.Entry<String, String> mapEntry : map.entrySet()) {
			EnvironmentEntry environmentEntry = new EnvironmentEntry();
			environmentEntry.key = mapEntry.getKey();
			environmentEntry.value = mapEntry.getValue();
			adaptedMap.environmentEntry.add(environmentEntry);
		}
		return adaptedMap;
	}

}
