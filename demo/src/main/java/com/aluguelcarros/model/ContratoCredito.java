package com.aluguelcarros.model;

import com.aluguelcarros.model.type.StatusCredito;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "contratos_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroCredito;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAprovado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCredito statusCredito;

    @OneToOne(optional = false)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banco_agente_id")
    private Agente bancoAgente;

    public Long getId() {
        return id;
    }

    public String getNumeroCredito() {
        return numeroCredito;
    }

    public BigDecimal getValorAprovado() {
        return valorAprovado;
    }

    public StatusCredito getStatusCredito() {
        return statusCredito;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public Agente getBancoAgente() {
        return bancoAgente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroCredito(String numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

    public void setValorAprovado(BigDecimal valorAprovado) {
        this.valorAprovado = valorAprovado;
    }

    public void setStatusCredito(StatusCredito statusCredito) {
        this.statusCredito = statusCredito;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public void setBancoAgente(Agente bancoAgente) {
        this.bancoAgente = bancoAgente;
    }
}
