package com.angeltashev.informatics.config.app;

import com.angeltashev.informatics.interceptors.ErrorInterceptor;
import com.angeltashev.informatics.interceptors.FaviconInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class AppWebConfig implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;
    private final ErrorInterceptor errorInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.faviconInterceptor);
        registry.addInterceptor(this.errorInterceptor);
    }
}
