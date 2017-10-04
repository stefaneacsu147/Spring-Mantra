package com.nttdata.myapp.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class JacksonConfiguration {

    /*
     * Support for Hibernate types in Jackson.
     */
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /*
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }
    
//    @Bean(name = "multipartResolver")
//  	public CommonsMultipartResolver multipartResolver() {
//  		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//  		multipartResolver.setMaxUploadSize(100000);
//  		return new CommonsMultipartResolver();
//  	}
    
}
