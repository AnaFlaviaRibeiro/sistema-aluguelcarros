package com.aluguelcarros.model;

import com.aluguelcarros.model.type.TipoProprietario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "automoveis")
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String matricula;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(nullable = false, length = 60)
    private String modelo;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoProprietario proprietarioTipo;

    @OneToOne(mappedBy = "automovel")
    private PedidoAluguel pedidoAluguel;

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public Integer getAno() {
        return ano;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoProprietario getProprietarioTipo() {
        return proprietarioTipo;
    }

    public PedidoAluguel getPedidoAluguel() {
        return pedidoAluguel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setProprietarioTipo(TipoProprietario proprietarioTipo) {
        this.proprietarioTipo = proprietarioTipo;
    }

    public void setPedidoAluguel(PedidoAluguel pedidoAluguel) {
        this.pedidoAluguel = pedidoAluguel;
    }
}
