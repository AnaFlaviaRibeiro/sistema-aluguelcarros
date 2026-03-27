package br.com.aluguelcarros.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agente")
public class Agente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_agente", nullable = false, length = 20)
    private TipoAgente tipoAgente;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @OneToMany(mappedBy = "agenteResponsavel", fetch = FetchType.LAZY)
    private List<PedidoAluguel> pedidosAnalisados = new ArrayList<>();

    @OneToMany(mappedBy = "bancoAgente", fetch = FetchType.LAZY)
    private List<ContratoCredito> creditosConcedidos = new ArrayList<>();

    public Agente() {
    }

    public Long getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public TipoAgente getTipoAgente() {
        return tipoAgente;
    }

    public String getCnpj() {
        return cnpj;
    }

    public List<PedidoAluguel> getPedidosAnalisados() {
        return pedidosAnalisados;
    }

    public List<ContratoCredito> getCreditosConcedidos() {
        return creditosConcedidos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setTipoAgente(TipoAgente tipoAgente) {
        this.tipoAgente = tipoAgente;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setPedidosAnalisados(List<PedidoAluguel> pedidosAnalisados) {
        this.pedidosAnalisados = pedidosAnalisados;
    }

    public void setCreditosConcedidos(List<ContratoCredito> creditosConcedidos) {
        this.creditosConcedidos = creditosConcedidos;
    }
}
