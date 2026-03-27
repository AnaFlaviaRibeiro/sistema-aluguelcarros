package br.com.aluguelcarros.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "rendimento")
public class Rendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entidade_empregadora", nullable = false, length = 120)
    private String entidadeEmpregadora;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Rendimento() {
    }

    public Long getId() {
        return id;
    }

    public String getEntidadeEmpregadora() {
        return entidadeEmpregadora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEntidadeEmpregadora(String entidadeEmpregadora) {
        this.entidadeEmpregadora = entidadeEmpregadora;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
