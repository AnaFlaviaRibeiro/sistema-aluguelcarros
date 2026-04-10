package com.aluguelcarros.bootstrap;

import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.type.TipoAgente;
import com.aluguelcarros.repository.AgenteRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
public class AgenteBootstrap implements ApplicationEventListener<StartupEvent> {

    private final AgenteRepository agenteRepository;
    private final PasswordEncoder passwordEncoder;

    public AgenteBootstrap(AgenteRepository agenteRepository, PasswordEncoder passwordEncoder) {
        this.agenteRepository = agenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(StartupEvent event) {
        if (agenteRepository.count() > 0) {
            return;
        }
        Agente locadora = new Agente();
        locadora.setNome("Agente Locadora Demo");
        locadora.setEmail("empresa@demo.com");
        locadora.setSenha(passwordEncoder.encode("agente123"));
        locadora.setCnpj("12345678000190");
        locadora.setNomeInstituicao("Locadora Exemplo S.A.");
        locadora.setTipoAgente(TipoAgente.EMPRESA);
        agenteRepository.save(locadora);

        Agente banco = new Agente();
        banco.setNome("Agente Banco Demo");
        banco.setEmail("banco@demo.com");
        banco.setSenha(passwordEncoder.encode("agente123"));
        banco.setCnpj("98765432000110");
        banco.setNomeInstituicao("Banco Exemplo S.A.");
        banco.setTipoAgente(TipoAgente.BANCO);
        agenteRepository.save(banco);
    }
}
