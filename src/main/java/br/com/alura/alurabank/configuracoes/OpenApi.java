package br.com.alura.alurabank.configuracoes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API para gerenciar contas correntes",
                description = "Com ela é possivel cadastrar contas correntes"
        ),
        security = {
                @SecurityRequirement(name = "authentication"),
        }
)
@SecurityScheme(
        name = "authentication",
        type = SecuritySchemeType.OAUTH2,
//        openIdConnectUrl = "http://localhost:8180/auth/realms/alura-bank/.well-known/openid-configuration"
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:8180/auth/realms/alura-bank/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:8180/auth/realms/alura-bank/protocol/openid-connect/token",
                        refreshUrl = "http://localhost:8180/auth/realms/alura-bank/protocol/openid-connect/token"
                )
        )

)
public class OpenApi {


//    @Bean
//    public OpenAPI openApi() {
//        return new OpenAPI().components();
//    }

}
