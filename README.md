scoped-properties-maven-plugin
==============================

a simple maven plugin (and ANT task) to handle multi scope developement environments.

# Description 

So many time I have projects with multiple target environments, my computer (developement), a virtual machine locally hosted by my company (pre-production), and the client environment (production).

All project need configurations and I used many time .properties files. It's easy to used and I have many utility tools in my company software. 

But I it's hard to manage many environment in this files and VCS. Consultant wants store the client properties files but where store the local hosted properties and the developer properties?

My solution is tu store only an XML file whose have all environments properties and build the .properties file during the compilation process. 


# Exemple
## custom.properties.xml (the input file)

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<properties>
    <property name="simple.property">
        <values>
            <environment scope="development">
                <value>scoped-properties-maven-plugin-development</value>
            </environment>
            <environment scope="production">
                <value>scoped-properties-maven-plugin</value>
            </environment>
        </values>
    </property>
    <property name="property.with.equals">
        <values>
            <environment scope="development">
                <value>https://git.famaridon.com?tab=repositories</value>
            </environment>
            <environment scope="production">
                <value>https://git.famaridon.com?tab=repositories</value>
            </environment>
        </values>
    </property>
    <property name="property.key with space">
        <values>
            <environment scope="development">
                <value>https://git.famaridon.com?tab=repositories</value>
            </environment>
            <environment scope="production">
                <value>https://git.famaridon.com?tab=repositories</value>
            </environment>
        </values>
    </property>
    <property name="property.value.with.unicode.char">
        <values>
            <environment scope="development">
                <value>ç Σ</value>
            </environment>
            <environment scope="production">
                <value>ç Σ</value>
            </environment>
        </values>
    </property>
    <property name="property.with.default.value">
        <default-value>a default value</default-value>
        <values>
            <environment scope="development">
                <value>a development value</value>
            </environment>
        </values>
    </property>
</properties>

```

## custom.properties (the output file)
```
#Maven plugin building file for scope : production
#Sat Jul 12 11:38:35 CEST 2014
property.value.with.unicode.char=\u00E7 \u03A3
property.with.equals=https\://git.famaridon.com?tab\=repositories
simple.property=scoped-properties-maven-plugin
property.key\ with\ space=https\://git.famaridon.com?tab\=repositories
property.with.default.value=a default value

```


# License

The MIT License (MIT) Copyright (c) 2014 Florent Amaridon and other contributors:


