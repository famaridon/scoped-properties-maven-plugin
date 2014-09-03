package com.famaridon.maven.scoped.properties.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation flag class as handler scanned by ScopedProperties.
 * Created by famaridon on 20/08/2014.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomHandler {
	String shortName();
}
