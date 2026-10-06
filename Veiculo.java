package com.frota.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aceita placa antiga (ABC-1234 / ABC1234) e Mercosul (ABC1D23)
    @NotBlank(message = "A placa e obrigatoria")
    @Pattern(regexp = "^[A-Za-z]{3}-?[0-9][A-Za-z0-9][0-9]{2}$",
             message = "Placa invalida (ex.: ABC-1234 ou ABC1D23)")
    @Column(nullable = false, unique = true)
    private String placa;

    @NotBlank(message = "A marca e obrigatoria")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "O modelo e obrigatorio")
    @Column(nullable = false)
    private String modelo;

    @NotNull(message = "O ano e obrigatorio")
    @Min(value = 1900, message = "Ano invalido")
    @Max(value = 2100, message = "Ano invalido")
    @Column(nullable = false)
    private Integer ano;

    public Veiculo() {
    }

    public Veiculo(String placa, String marca, String modelo, Integer ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
}
