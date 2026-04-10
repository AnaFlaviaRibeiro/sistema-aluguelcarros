package com.aluguelcarros.model;

import com.aluguelcarros.model.type.TipoContrato;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroContrato;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoContrato tipoContrato;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_aluguel_id")
    private PedidoAluguel pedidoAluguel;

    @OneToOne(mappedBy = "contrato", cascade = CascadeType.ALL)
    private ContratoCredito contratoCredito;

    public Long getId() {
        return id;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public PedidoAluguel getPedidoAluguel() {
        return pedidoAluguel;
    }

    public ContratoCredito getContratoCredito() {
        return contratoCredito;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public void setPedidoAluguel(PedidoAluguel pedidoAluguel) {
        this.pedidoAluguel = pedidoAluguel;
    }

    public void setContratoCredito(ContratoCredito contratoCredito) {
        this.contratoCredito = contratoCredito;
    }
}
