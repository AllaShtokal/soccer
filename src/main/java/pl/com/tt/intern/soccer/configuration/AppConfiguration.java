package pl.com.tt.intern.soccer.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AppConfiguration {

    @Value("${server.default.timezone}")
    private String defaultTimeZone;

    private static final String RESOLVER_PREFIX = "classpath:/docs/mail/";
    private static final String RESOLVER_SUFFIX = ".html";

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    final ApplicationContext applicationContext;

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(RESOLVER_PREFIX);
        templateResolver.setSuffix(RESOLVER_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setApplicationContext(applicationContext);
        return templateResolver;
    }
}
