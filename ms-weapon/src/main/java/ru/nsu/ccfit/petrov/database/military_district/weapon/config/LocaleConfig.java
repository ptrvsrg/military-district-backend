package ru.nsu.ccfit.petrov.database.military_district.weapon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleResolver localeResolver() {
    return new AcceptHeaderLocaleResolver();
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setUseCodeAsDefaultMessage(true);
    source.setDefaultEncoding("UTF-8");
    source.setBasename("lang/messages");
    return source;
  }
}
