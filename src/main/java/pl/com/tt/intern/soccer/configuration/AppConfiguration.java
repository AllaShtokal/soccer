package pl.com.tt.intern.soccer.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AppConfiguration {

    @Value("${server.default.timezone}")
    private String defaultTimeZone;

    private final String resolverPrefix = "classpath:/docs/mail/";
    private final String resolverSuffix = ".html";

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Autowired
    ApplicationContext applicationContext;

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
        templateResolver.setPrefix(resolverPrefix);
        templateResolver.setSuffix(resolverSuffix);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setApplicationContext(applicationContext);
        return templateResolver;
    }
}
