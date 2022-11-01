package com.lyf.config;

import com.lyf.interceptor.ResourcesInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.List;

@Controller
public class ServletConfig extends WebMvcConfigurationSupport {
    @Value("#{'${ignnoreUrl}'.split(',')}")
    private List<String> ignoreUrl;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResourcesInterceptor(ignoreUrl))
                                        .addPathPatterns("/**")
                                        .excludePathPatterns("/css/**", "/js/**", "/img/**");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/admin/", ".jsp");
    }
}
