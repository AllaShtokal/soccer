package pl.com.tt.intern.soccer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String basePackage = "pl.com.tt.intern.soccer";

    @Bean
    public Docket docket() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

}
