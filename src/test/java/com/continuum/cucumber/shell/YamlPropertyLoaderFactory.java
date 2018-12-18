package com.continuum.cucumber.shell;

import lombok.val;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {
    public static PropertySource<?> getPropertySource(EncodedResource resource) throws IOException {
        val res = resource.getResource();
        val commandLineProperties = System.getProperties();

        val prop = new YamlPropertySourceLoader().load(res.getFilename(), res);

        val spring = "spring.profiles.active";
        val active = commandLineProperties.containsKey(spring) ? commandLineProperties.getProperty(spring) :
                prop.stream().map(p -> p.getProperty(spring)).filter(Objects::nonNull).findFirst().orElse(null);
        val propertySource = prop.stream().filter(p -> active.equals(p.getProperty("spring.profiles"))).findFirst().orElse(null);

        val yamlPropertiesMap = (LinkedHashMap<String, OriginTrackedValue>) propertySource.getSource();

        yamlPropertiesMap.entrySet().stream().filter(entry -> commandLineProperties.containsKey(entry.getKey()))
                .forEach(entry -> entry.setValue(OriginTrackedValue.of(commandLineProperties.getProperty(entry.getKey()))));

        yamlPropertiesMap.entrySet().stream().filter(entry -> !commandLineProperties.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue().toString()))
                .forEach(System::setProperty);

        return propertySource;
    }

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        return resource == null ? super.createPropertySource(name, resource) : getPropertySource(resource);
    }
}