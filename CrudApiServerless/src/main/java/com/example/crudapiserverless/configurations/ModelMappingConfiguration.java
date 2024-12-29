package com.example.crudapiserverless.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMappingConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}