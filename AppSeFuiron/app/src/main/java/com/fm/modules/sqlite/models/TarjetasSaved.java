package com.fm.modules.sqlite.models;

import com.fm.modules.models.Usuario;

public class TarjetasSaved {
    private String tarjUsuario_id;
    private String numTarjeta;
    private String codTarjeta;
    private Usuario usuario;
    private String tipo;

    public TarjetasSaved() {
    }

    public TarjetasSaved(String tarjUsuario_id, String numTarjeta, String codTarjeta, Usuario usuario, String tipo) {
        this.tarjUsuario_id = tarjUsuario_id;
        this.numTarjeta = numTarjeta;
        this.codTarjeta = codTarjeta;
        this.usuario = usuario;
        this.tipo = tipo;
    }

    public String getTarjUsuario_id() {
        return tarjUsuario_id;
    }

    public void setTarjUsuario_id(String tarjUsuario_id) {
        this.tarjUsuario_id = tarjUsuario_id;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getCodTarjeta() {
        return codTarjeta;
    }

    public void setCodTarjeta(String codTarjeta) {
        this.codTarjeta = codTarjeta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
