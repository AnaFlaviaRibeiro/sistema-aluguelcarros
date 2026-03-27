package br.com.aluguelcarros.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automovel")
public class Automovel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula", nullable = false, unique = true, length = 30)
    private String matricula;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "marca", nullable = false, length = 60)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 60)
    private String modelo;

    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", nullable = false, length = 20)
    private TipoProprietario tipoProprietario;

    @OneToMany(mappedBy = "automovel", fetch = FetchType.LAZY)
    private List<PedidoAluguel> pedidos = new ArrayList<>();

    public Automovel() {
    }

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

    public TipoProprietario getTipoProprietario() {
        return tipoProprietario;
    }

    public List<PedidoAluguel> getPedidos() {
        return pedidos;
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

    public void setTipoProprietario(TipoProprietario tipoProprietario) {
        this.tipoProprietario = tipoProprietario;
    }

    public void setPedidos(List<PedidoAluguel> pedidos) {
        this.pedidos = pedidos;
    }
}