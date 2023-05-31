package no.fintlabs.security;

import no.vigoiks.resourceserver.security.FintJwtUserConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                        .pathMatchers("/**")
                        .access(new OpaAuthorizationManager())
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt()
                        .jwtAuthenticationConverter(new FintJwtUserConverter()));
        return http.build();
    }
}
