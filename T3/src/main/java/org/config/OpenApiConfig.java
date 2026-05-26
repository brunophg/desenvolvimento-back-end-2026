package org.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configura os dados principais exibidos no Swagger/OpenAPI.
@Configuration
public class OpenApiConfig {

    // Registra a documentacao padrao da API no contexto Spring.
    @Bean
    public OpenAPI aulaDbeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // Nome exibido no topo da documentacao.
                        .title("G2-T3 API")
                        // Texto curto explicando o objetivo da API.
                        .description("Documentacao padrao Swagger/OpenAPI das APIs publicadas no projeto do Grupo 2.")
                        // Versao atual da documentacao publicada.
                        .version("v1")
                        .contact(new Contact()
                                // Contato exibido na documentacao.
                                .name("Projeto Aula DBE")
                                .email("suporte@aula.local"))
                        .license(new License()
                                // Tipo de uso informado para a API.
                                .name("Uso academico")
                                .url("https://example.local/licenca")));
    }
}
