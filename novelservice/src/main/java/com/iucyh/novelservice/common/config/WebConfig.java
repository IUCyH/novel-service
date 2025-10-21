package com.iucyh.novelservice.common.config;

import com.iucyh.novelservice.common.converter.StringToEnumConverterFactory;
import com.iucyh.novelservice.common.converter.StringToUUIDConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
        registry.addConverter(new StringToUUIDConverter());
    }
}
