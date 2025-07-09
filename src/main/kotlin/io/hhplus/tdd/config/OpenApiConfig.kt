package io.hhplus.tdd.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("HH+ Point API")
                    .description("포인트 관리 시스템 API")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("성승현")
                            .email("ite476@gmail.com")
                    )
            )
    }
} 