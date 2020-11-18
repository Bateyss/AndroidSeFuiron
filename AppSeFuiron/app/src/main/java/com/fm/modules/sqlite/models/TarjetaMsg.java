package com.fm.modules.sqlite.models;

public class TarjetaMsg {
    private String codigo;
    private String mensaje;
    private String descripcion;

    public TarjetaMsg() {
    }

    public TarjetaMsg(String codigo, String mensaje, String descripcion) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
