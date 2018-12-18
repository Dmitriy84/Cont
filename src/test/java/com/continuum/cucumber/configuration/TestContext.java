package com.continuum.cucumber.configuration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class TestContext {
    private Map<String, ? super Object> values = new HashMap<>();

    public void write(String key, Object value) {
        values.put(key, value);
    }

    public <T> T read(String key, Class<T> valueType) {
        if (values.containsKey(key) && valueType.isInstance(values.get(key)))
            return valueType.cast(values.get(key));
        throw new NoSuchElementException("No stored value with key <" + key + "> and type <" + valueType.getCanonicalName() + ">");
    }

    public String readString(String key) {
        return read(key, String.class);
    }

    public boolean isValuePresent(String key) {
        return values.containsKey(key);
    }
}
