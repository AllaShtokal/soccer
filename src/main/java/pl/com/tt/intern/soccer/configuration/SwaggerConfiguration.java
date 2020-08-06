package pl.com.tt.intern.soccer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static io.swagger.models.auth.In.HEADER;
import static java.util.Collections.singletonList;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String BASE_PACKAGE = "pl.com.tt.intern.soccer";

    @Bean
    public Docket docket() {
        return new Docket(SWAGGER_2)
                .ignoredParameterTypes(CurrentUser.class)
                .securitySchemes(singletonList(new ApiKey("JWT", AUTHORIZATION, HEADER.name())))
                .securityContexts(singletonList(
                        SecurityContext.builder()
                                .securityReferences(
                                        singletonList(SecurityReference.builder()
                                                .reference("JWT")
                                                .scopes(new AuthorizationScope[0])
                                                .build()
                                        )
                                )
                                .build())
                )
                .select()
                .apis(basePackage(BASE_PACKAGE))
                .paths(any())
                .build();
    }
}
