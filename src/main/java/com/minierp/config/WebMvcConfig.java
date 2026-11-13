package com.minierp.config;

import com.minierp.interceptor.SessionInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

/**
 * Spring MVC web layer configuration.
 * Configures ViewResolver, static resources, interceptors.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.minierp.controller",
        "com.minierp.rest"
})
public class WebMvcConfig implements WebMvcConfigurer {

    // ------------------------------------------------------------------ //
    // JSP View Resolver                                                   //
    // ------------------------------------------------------------------ //
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1);
        return resolver;
    }

    // ------------------------------------------------------------------ //
    // Static Resources                                                    //
    // ------------------------------------------------------------------ //
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .setCachePeriod(3600);
    }

    // ------------------------------------------------------------------ //
    // Session Interceptor – protects all pages except /login              //
    // ------------------------------------------------------------------ //
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/api/login",
                        "/resources/**",
                        "/error/**"
                );
    }

    // ------------------------------------------------------------------ //
    // Message Converters – JSON for REST                                  //
    // ------------------------------------------------------------------ //
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
    }
}
