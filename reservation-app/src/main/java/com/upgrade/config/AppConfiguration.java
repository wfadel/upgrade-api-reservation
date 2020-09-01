package com.upgrade.config;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class AppConfiguration {

    @Bean
    public ConversionServiceFactoryBean conversionService(Set<Converter> converters) {
        ConversionServiceFactoryBean service = new ConversionServiceFactoryBean();
        service.setConverters(converters);
        service.afterPropertiesSet();
        return service;
    }
}
