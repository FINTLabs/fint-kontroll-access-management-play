package no.fintlabs.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.vigoiks.resourceserver.security.FintJwtEndUserPrincipal;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class OpaAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .map(a -> {
                    JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) a;
                    Jwt principal = (Jwt) jwtToken.getPrincipal();
                    FintJwtEndUserPrincipal from = FintJwtEndUserPrincipal.from(principal);
                    String principalName = (String) principal.getClaims().get("principalName");
                    log.info("Fant brukernavn {}", principalName);

                    ServerHttpRequest request = context.getExchange().getRequest();
                    log.info("Request method {}", request.getMethod());
                    log.info("Request path {}", request.getPath());

                    // Kall til OPA
                    boolean authenticated = a.isAuthenticated();
                    log.info("Authenticated {}", authenticated);
                    boolean isAuthorized = true;
                    return new AuthorizationDecision(isAuthorized);
                })
                .defaultIfEmpty(new AuthorizationDecision(true))
                .doOnError(error -> System.out.println("An error occured voting decision"));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        System.out.println("OpaAuthorizationManager.verify");
        return ReactiveAuthorizationManager.super.verify(authentication, object);
    }
}
