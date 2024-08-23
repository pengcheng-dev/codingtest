package com.bgl.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Autowired
    private transient RequestInfoLoggingInjector requestInfoLoggingInjector;

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    /**
     *
     * ensure response json
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    /**
     * Add injector for a request to log request and response info
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestInfoLoggingInjector);
    }

    /**
     * fix CORS problem
     * @param registry
     */
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins) // Adjust according to your client URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
