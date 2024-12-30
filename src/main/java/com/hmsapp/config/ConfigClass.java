package com.hmsapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ConfigClass {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
