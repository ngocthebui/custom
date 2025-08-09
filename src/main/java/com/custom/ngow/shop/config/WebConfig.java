package com.custom.ngow.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    // CSS
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

    // JS
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

    // Images
    registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");

    // Fonts
    registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");

    // Icons
    registry.addResourceHandler("/icon/**").addResourceLocations("classpath:/static/icon/");
  }
}
