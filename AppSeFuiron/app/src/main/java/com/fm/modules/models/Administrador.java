package com.fm.modules.models;


import java.util.Date;


public class Administrador {

    private long administradorId;
    private String username;
    private String password;
    private Date ultimoInicio;
    private Date fechaCreacion;

    public Administrador() {
    }

    public Administrador(long administradorId, String username, String password, Date ultimoInicio,
                         Date fechaCreacion) {
        this.administradorId = administradorId;
        this.username = username;
        this.password = password;
        this.ultimoInicio = ultimoInicio;
        this.fechaCreacion = fechaCreacion;
    }

    public long getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(long administradorId) {
        this.administradorId = administradorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getUltimoInicio() {
        return ultimoInicio;
    }

    public void setUltimoInicio(Date ultimoInicio) {
        this.ultimoInicio = ultimoInicio;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{administradorId:'");
        builder.append(administradorId);
        builder.append("',username:'");
        builder.append(username);
        builder.append("',password:'");
        builder.append(password);
        builder.append("',ultimoInicio:'");
        builder.append(ultimoInicio);
        builder.append("',fechaCreacion:'");
        builder.append(fechaCreacion);
        builder.append("'}");
        return builder.toString();
    }

}
