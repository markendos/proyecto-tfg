package es.upo.witzl.proyectotfg.spring;

import es.upo.witzl.proyectotfg.users.security.LoginPageInterceptor;
import es.upo.witzl.proyectotfg.users.validation.EmailValidator;
import es.upo.witzl.proyectotfg.users.validation.PasswordMatchesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletContext;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "es.upo.witzl.proyectotfg")
public class MvcConfig implements WebMvcConfigurer {

    public MvcConfig() {
        super();
    }

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext context;

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
        registry.addViewController("/badUser");
        registry.addViewController("/changePassword");
        registry.addViewController("/console");
        registry.addViewController("/emailError");
        registry.addViewController("/error");
        registry.addViewController("/global");
        registry.addViewController("/home");
        registry.addViewController("/invalidSession");
        registry.addViewController("/login");
        registry.addViewController("/logout");
        registry.addViewController("/project");
        registry.addViewController("/registerSample");
        registry.addViewController("/resetPassword");
        registry.addViewController("/signUp");
        registry.addViewController("/successRegister");
        registry.addViewController("/updatePassword");
        registry.addViewController("/users");
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/resources/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/static/js/");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(new LoginPageInterceptor());
    }

    // beans

    @Bean
    public LocaleContextResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieName("language");
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

    @Bean
    public EmailValidator usernameValidator() {
        return new EmailValidator();
    }

    @Bean
    public PasswordMatchesValidator passwordMatchesValidator() {
        return new PasswordMatchesValidator();
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

}
