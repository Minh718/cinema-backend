package com.movie.apigateway.configurations;

import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GatewayAuthFilter implements GlobalFilter {

    private final ReactiveJwtDecoder jwtDecoder;

    public GatewayAuthFilter(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String[] tokenHolder = new String[1];

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenHolder[0] = authHeader.substring(7);
        }

        if ((tokenHolder[0] == null || tokenHolder[0].isBlank())) {
            tokenHolder[0] = request.getQueryParams().getFirst("token");
        }

        if (tokenHolder[0] == null || tokenHolder[0].isBlank()) {
            return chain.filter(exchange);
        }

        return jwtDecoder.decode(tokenHolder[0])
                .flatMap(jwt -> {
                    String userId = jwt.getSubject();
                    // String email = jwt.getClaimAsString("email");
                    List<String> roles = jwt.hasClaim("realm_access")
                            ? (List<String>) ((Map<String, Object>) jwt.getClaim("realm_access")).get("roles")
                            : List.of();

                    ServerHttpRequest mutatedRequest = request
                            .mutate()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenHolder[0])
                            .header("X-User-Id", userId)
                            // .header("X-User-Email", email != null ? email : "")
                            .header("X-User-Roles", String.join(",", roles))
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}