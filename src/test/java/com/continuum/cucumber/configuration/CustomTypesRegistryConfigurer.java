package com.continuum.cucumber.configuration;

import com.continuum.cucumber.SpringBootCucumberApplication;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.CaptureGroupTransformer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.cucumberexpressions.TypeReference;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.emory.mathcs.backport.java.util.Collections.singletonList;
import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;

public class CustomTypesRegistryConfigurer implements TypeRegistryConfigurer {
    @Override
    @SuppressWarnings("unchecked")
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        getListOfEnumClasses(Enum.class).forEach(e ->
                {
                    //add Enum
                    typeRegistry.defineParameterType(ParameterType.fromEnum(e));

                    final Transformer<List> transformer = s -> (List) Arrays.stream(s.split(", ")).map(st ->
                            Enum.valueOf(e, st)).collect(Collectors.toList());
                    //add List<Enum>
                    typeRegistry.defineParameterType(new ParameterType<>(
                            e.getSimpleName() + "List",
                            ".*?",
                            List.class,
                            transformer)
                    );
                }
        );

        StepsEnums.externalEnums.forEach(cls -> typeRegistry.defineParameterType(ParameterType.fromEnum(cls)));

        typeRegistry.defineParameterType(new ParameterType<>(
                "strings",
                singletonList("\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(\\\\.[^'\\\\]*)*)'"),
                new TypeReference<List<String>>() {
                }.getType(),
                (CaptureGroupTransformer<List<String>>) s ->
                        asList(s[0]
                                .replaceAll("\\\\\"", "\"")
                                .replaceAll("\\\\'", "'")
                                .split(", ")),
                false,
                false)
        );
    }

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    private Set<Class<? extends Enum>> getListOfEnumClasses(Class<? extends Enum> clazz) {
        return new Reflections(SpringBootCucumberApplication.PACKAGES, new SubTypesScanner(), new TypeAnnotationsScanner())
                .getSubTypesOf(clazz).stream().filter(cls -> cls.isAnnotationPresent(TypeRegistryConfigurerAnnotation.class)).collect(Collectors.toSet());
    }
}