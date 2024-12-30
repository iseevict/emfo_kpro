package kr.co.emfo.kpro_test.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI EmfoAPI() {
        Info info = new Info()
                .title("EMFO API")
                .description("EMFO API 명세서")
                .version("1.0.0");

        String HEADER_NAME = "EMFO";
        Components components = new Components()
                .addSecuritySchemes(HEADER_NAME, new SecurityScheme()
                        .name(HEADER_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(HEADER_NAME))
                .components(components);
    }
}
