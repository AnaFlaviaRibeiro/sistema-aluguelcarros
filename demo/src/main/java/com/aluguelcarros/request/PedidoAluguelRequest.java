package com.aluguelcarros.request;

import com.aluguelcarros.model.type.TipoProprietario;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Serdeable
@Introspected
public class PedidoAluguelRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private BigDecimal valorMensal;

    @NotNull
    @Min(1)
    private Integer prazoMeses;

    @NotBlank
    private String matricula;

    @NotNull
    private Integer ano;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    private String placa;

    @NotNull
    private TipoProprietario proprietarioTipo;

    public Long getClienteId() {
        return clienteId;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
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

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
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
}
