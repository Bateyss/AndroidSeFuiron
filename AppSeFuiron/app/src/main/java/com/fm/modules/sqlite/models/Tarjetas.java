package com.fm.modules.sqlite.models;

public class Tarjetas {
    private String numTarjeta;
    private String fechaTarjeta;
    private String codSecu;
    private Double monto;
    private Long usuario;

    public Tarjetas() {
    }

    public Tarjetas(String numTarjeta, String fechaTarjeta, String codSecu, Double monto, Long usuario) {
        this.numTarjeta = numTarjeta;
        this.fechaTarjeta = fechaTarjeta;
        this.codSecu = codSecu;
        this.monto = monto;
        this.usuario = usuario;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getFechaTarjeta() {
        return fechaTarjeta;
    }

    public void setFechaTarjeta(String fechaTarjeta) {
        this.fechaTarjeta = fechaTarjeta;
    }

    public String getCodSecu() {
        return codSecu;
    }

    public void setCodSecu(String codSecu) {
        this.codSecu = codSecu;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
