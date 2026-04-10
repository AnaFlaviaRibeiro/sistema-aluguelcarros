package com.aluguelcarros.model;
import com.aluguelcarros.model.type.StatusPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos_aluguel")
public class PedidoAluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorMensal;

    @Column(nullable = false)
    private Integer prazoMeses;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @OneToOne(mappedBy = "pedidoAluguel", cascade = CascadeType.ALL)
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "agente_avaliador_id")
    private Agente agenteAvaliador;

    public Long getId() {
        return id;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public Agente getAgenteAvaliador() {
        return agenteAvaliador;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public void setAgenteAvaliador(Agente agenteAvaliador) {
        this.agenteAvaliador = agenteAvaliador;
    }
}