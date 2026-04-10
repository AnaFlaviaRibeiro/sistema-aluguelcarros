package com.aluguelcarros.service;

import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Usuario;
import com.aluguelcarros.repository.UsuarioRepository;
import com.aluguelcarros.request.LoginRequest;
import com.aluguelcarros.response.LoginResponse;
import com.aluguelcarros.security.JwtService;
import jakarta.inject.Singleton;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inv\u00e1lidas."));
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Credenciais inv\u00e1lidas.");
        }
        String token = jwtService.gerarToken(usuario.getId(), usuario.getTipo());
        String tipoAgente = null;
        if (usuario instanceof Agente agente) {
            tipoAgente = agente.getTipoAgente().name();
        }
        return new LoginResponse(token, usuario.getTipo(), usuario.getId(), usuario.getNome(), tipoAgente);
    }
}
