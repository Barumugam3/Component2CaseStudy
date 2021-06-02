package com.iiht.estock.edge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProxyApiConfig {

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList();
            resources.add(createResource("company-service", "company-service", "3.0"));
            resources.add(createResource("stock-command-service", "stock-command-service", "3.0"));
            resources.add(createResource("stock-query-service", "stock-query-service", "3.0"));
            return resources;
        };
    }

    private SwaggerResource createResource(String serviceId, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(serviceId);
        swaggerResource.setLocation("/" + location + "/v3/api-docs");
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
