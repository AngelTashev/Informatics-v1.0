package com.angeltashev.informatics.config.app;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
