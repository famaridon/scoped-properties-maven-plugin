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
public class HandlerConfigurationAdapter extends XmlAdapter<HandlerConfiguration, Map<String, Object>> {

	@Override
	public Map<String, Object> unmarshal(HandlerConfiguration adaptedMap) throws Exception {
		Map<String, Object> map = new HashMap<>();
		for (HandlerConfigurationEntry environmentEntry : adaptedMap.handlerConfigurationEntries) {
			map.put(environmentEntry.handler, environmentEntry.configuration);
		}
		return map;
	}

	@Override
	public HandlerConfiguration marshal(Map<String, Object> map) throws Exception {
		HandlerConfiguration adaptedMap = new HandlerConfiguration();
		for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
			HandlerConfigurationEntry environmentEntry = new HandlerConfigurationEntry();
			environmentEntry.handler = mapEntry.getKey();
			environmentEntry.configuration = mapEntry.getValue();
			adaptedMap.handlerConfigurationEntries.add(environmentEntry);
		}
		return adaptedMap;
	}

}
