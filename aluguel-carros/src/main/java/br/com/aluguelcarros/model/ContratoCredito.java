package br.com.aluguelcarros.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "contrato_credito")
public class ContratoCredito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contrato_id", nullable = false, unique = true)
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "banco_agente_id", nullable = false)
    private Agente bancoAgente;

    @Column(name = "valor_credito", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorCredito;

    @Column(name = "taxa_juros", nullable = false, precision = 8, scale = 4)
    private BigDecimal taxaJuros;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    public ContratoCredito() {
    }

    public Long getId() {
        return id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public Agente getBancoAgente() {
        return bancoAgente;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public BigDecimal getTaxaJuros() {
        return taxaJuros;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public void setBancoAgente(Agente bancoAgente) {
        this.bancoAgente = bancoAgente;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public void setTaxaJuros(BigDecimal taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }
}
