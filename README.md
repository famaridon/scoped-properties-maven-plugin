scoped-properties-maven-plugin
==============================

a simple maven plugin (and ANT task) to handle multi scope developement environments.


## Exemple
### source.properties.xml

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<properties>
    <property name="com.vdoc">
        <values>
            <environment scope="developement">
                <value>valeur developement</value>
            </environment>
            <environment scope="pre-production">
                <value>valeur pre-production</value>
            </environment>
            <environment scope="production">
                <value>valeur production</value>
            </environment>
        </values>
    </property>
    <property name="tft.server.url">
        <values>
            <environment scope="developement">
                <value>http://developement</value>
            </environment>
            <environment scope="pre-production">
                <value>http://pre-production</value>
            </environment>
            <environment scope="production">
                <value>http://production</value>
            </environment>
        </values>
    </property>
</properties>

```


## License

The MIT License (MIT) Copyright (c) 2014 Florent Amaridon and other contributors:


