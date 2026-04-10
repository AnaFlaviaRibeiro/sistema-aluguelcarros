package com.aluguelcarros.model;

import com.aluguelcarros.model.type.TipoAgente;
import com.aluguelcarros.model.type.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agentes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Agente extends Usuario {

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false, length = 150)
    private String nomeInstituicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAgente tipoAgente;

    @OneToMany(mappedBy = "agenteAvaliador")
    private List<PedidoAluguel> pedidosAvaliados = new ArrayList<>();

    @OneToMany(mappedBy = "bancoAgente")
    private List<ContratoCredito> contratosCredito = new ArrayList<>();

    public Agente() {
        setTipo(TipoUsuario.AGENTE);
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public TipoAgente getTipoAgente() {
        return tipoAgente;
    }

    public List<PedidoAluguel> getPedidosAvaliados() {
        return pedidosAvaliados;
    }

    public List<ContratoCredito> getContratosCredito() {
        return contratosCredito;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }

    public void setTipoAgente(TipoAgente tipoAgente) {
        this.tipoAgente = tipoAgente;
    }

    public void setPedidosAvaliados(List<PedidoAluguel> pedidosAvaliados) {
        this.pedidosAvaliados = pedidosAvaliados;
    }

    public void setContratosCredito(List<ContratoCredito> contratosCredito) {
        this.contratosCredito = contratosCredito;
    }
}
