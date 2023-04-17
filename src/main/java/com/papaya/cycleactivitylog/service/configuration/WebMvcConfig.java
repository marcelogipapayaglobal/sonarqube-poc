package com.papaya.cycleactivitylog.service.configuration;

import com.papaya.cycleactivitylog.service.apis.RequestInterceptor;
import com.papaya.cycleactivitylog.service.apis.v1.converters.LoggedItemToLoggedItemResponseConverter;
import com.papaya.cycleactivitylog.service.apis.v1.converters.PageToPageResponseConverter;
import com.papaya.cycleactivitylog.service.apis.v1.converters.PeriodRequestToPeriodConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PeriodRequestToPeriodConverter());
        registry.addConverter(new LoggedItemToLoggedItemResponseConverter());
        registry.addConverter(new PageToPageResponseConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/cycle-activity-log/v3/api-docs/**")
                .excludePathPatterns("/cycle-activity-log/swagger-ui/**")
                .excludePathPatterns("/actuator/**");
    }

}