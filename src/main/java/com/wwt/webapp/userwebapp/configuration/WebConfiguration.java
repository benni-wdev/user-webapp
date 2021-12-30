/*
 *  Copyright 2021  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // All resources go to where they should go
        registry
                .addResourceHandler( "/**/*.jsx", "/**/*.png", "/**/*.ttf"
                        ,"/**/*.ico","/**/*.gif","/**/*.svg", "/**/*.json","/**/*.woff", "/**/*.woff2","/**/*.css", "/**/*.html", "/**/*.js","/**/*.jpg"
                )
                .setCachePeriod(0)
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/", "/**")
                .setCachePeriod(0)
                .addResourceLocations("classpath:/static/index.html")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) {
                        if (resourcePath.startsWith("/rest")||resourcePath.startsWith("rest")||resourcePath.startsWith("/actuator")||resourcePath.startsWith("actuator")) {
                            return null;
                        }
                        return location.exists() && location.isReadable() ? location : null;
                    }
                });
    }
}
