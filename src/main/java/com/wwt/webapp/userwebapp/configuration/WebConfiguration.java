/* Copyright 2018-2021 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
