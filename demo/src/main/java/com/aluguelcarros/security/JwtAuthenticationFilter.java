package com.aluguelcarros.security;

import com.aluguelcarros.model.type.TipoUsuario;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

@Filter({"/cliente/**", "/agente/**"})
public class JwtAuthenticationFilter implements HttpServerFilter {

    private final JwtService jwtService;

    @Inject
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String header = request.getHeaders().getAuthorization().orElse(null);
        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return Publishers.just(HttpResponse.unauthorized());
        }
        String token = header.substring(7).trim();
        AuthPrincipal principal;
        try {
            principal = jwtService.parse(token);
        } catch (Exception e) {
            return Publishers.just(HttpResponse.unauthorized());
        }
        String path = request.getPath();
        if (path.startsWith("/cliente") && principal.tipo() != TipoUsuario.CLIENTE) {
            return Publishers.just(HttpResponse.status(HttpStatus.FORBIDDEN, "Acesso exclusivo de cliente."));
        }
        if (path.startsWith("/agente") && principal.tipo() != TipoUsuario.AGENTE) {
            return Publishers.just(HttpResponse.status(HttpStatus.FORBIDDEN, "Acesso exclusivo de agente."));
        }
        request.setAttribute(AuthPrincipal.ATTRIBUTE, principal);
        return chain.proceed(request);
    }
}
