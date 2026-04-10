package com.aluguelcarros.security;

import com.aluguelcarros.model.type.TipoUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Singleton
public class JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-seconds:28800}") long expirationSeconds) {
        if (secret == null || secret.length() < 32) {
            LOG.warn("jwt.secret deve ter pelo menos 32 caracteres; ajuste application.properties");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(bytes.length >= 32 ? bytes : padSecret(bytes));
        this.expirationMs = expirationSeconds * 1000L;
    }

    private static byte[] padSecret(byte[] b) {
        byte[] out = new byte[32];
        System.arraycopy(b, 0, out, 0, Math.min(b.length, 32));
        return out;
    }

    public String gerarToken(Long userId, TipoUsuario tipo) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", tipo.name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    public AuthPrincipal parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);
        TipoUsuario tipo = TipoUsuario.valueOf(role);
        return new AuthPrincipal(userId, tipo);
    }
}
