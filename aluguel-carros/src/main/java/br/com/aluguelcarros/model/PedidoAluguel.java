package br.com.aluguelcarros.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "pedido_aluguel")
public class PedidoAluguel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "automovel_id", nullable = false)
    private Automovel automovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_responsavel_id")
    private Agente agenteResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPedido status;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contrato contrato;

    public PedidoAluguel() {
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public Agente getAgenteResponsavel() {
        return agenteResponsavel;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public String getObservacao() {
        return observacao;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public void setAgenteResponsavel(Agente agenteResponsavel) {
        this.agenteResponsavel = agenteResponsavel;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}
