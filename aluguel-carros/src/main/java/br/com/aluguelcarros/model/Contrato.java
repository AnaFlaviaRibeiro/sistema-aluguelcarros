package br.com.aluguelcarros.model;

import br.com.aluguelcarros.model.type.TipoContrato;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
public class Contrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private PedidoAluguel pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false, length = 30)
    private TipoContrato tipoContrato;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "assinado", nullable = false)
    private Boolean assinado = false;

    @OneToOne(mappedBy = "contrato", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ContratoCredito contratoCredito;

    public Contrato() {
    }

    public Long getId() {
        return id;
    }

    public PedidoAluguel getPedido() {
        return pedido;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Boolean getAssinado() {
        return assinado;
    }

    public ContratoCredito getContratoCredito() {
        return contratoCredito;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(PedidoAluguel pedido) {
        this.pedido = pedido;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setAssinado(Boolean assinado) {
        this.assinado = assinado;
    }

    public void setContratoCredito(ContratoCredito contratoCredito) {
        this.contratoCredito = contratoCredito;
    }
}