package com.continuum.cucumber.template.step;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public abstract class AbstractBaseStepDefinition {
    public <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz) throws IOException {
        log.info("Retreiving value for class {}", clazz);

        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonFromResponse, clazz);
    }
}