package com.movie.paymentservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown fields
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Exclude nulls

        return mapper;
    }
}