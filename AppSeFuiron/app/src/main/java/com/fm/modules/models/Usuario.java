package com.fm.modules.models;

import java.util.Arrays;
import java.util.Date;

public class Usuario {

    private long usuarioId;
    private String username;
    private String password;
    private boolean habilitado;
    private String regPago;
    private String direccion;
    private Date ultimoInicio;
    private Date fechaCreacion;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private Date fechaDeMacimiento;
    private String celular;
    private byte[] imagenDePerfil;

    public Usuario() {
    }

    public Usuario(long usuarioId, String username, String password, boolean habilitado, String regPago, String direccion,
                   Date ultimoInicio, Date fechaCreacion, String nombre, String apellido, String correoElectronico,
                   Date fechaDeMacimiento, String celular, byte[] imagenDePerfil) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.password = password;
        this.habilitado = habilitado;
        this.regPago = regPago;
        this.direccion = direccion;
        this.ultimoInicio = ultimoInicio;
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.fechaDeMacimiento = fechaDeMacimiento;
        this.celular = celular;
        this.imagenDePerfil = imagenDePerfil;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
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

    public boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getRegPago() {
        return regPago;
    }

    public void setRegPago(String regPago) {
        this.regPago = regPago;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Date getFechaDeMacimiento() {
        return fechaDeMacimiento;
    }

    public void setFechaDeMacimiento(Date fechaDeMacimiento) {
        this.fechaDeMacimiento = fechaDeMacimiento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public byte[] getImagenDePerfil() {
        return imagenDePerfil;
    }

    public void setImagenDePerfil(byte[] imagenDePerfil) {
        this.imagenDePerfil = imagenDePerfil;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{usuarioId:'");
        builder.append(usuarioId);
        builder.append("',username:'");
        builder.append(username);
        builder.append("',password:'");
        builder.append(password);
        builder.append("',habilitado:'");
        builder.append(habilitado);
        builder.append("',regPago:'");
        builder.append(regPago);
        builder.append("',direccion:'");
        builder.append(direccion);
        builder.append("',ultimoInicio:'");
        builder.append(ultimoInicio);
        builder.append("',fechaCreacion:'");
        builder.append(fechaCreacion);
        builder.append("',nombre:'");
        builder.append(nombre);
        builder.append("',apellido:'");
        builder.append(apellido);
        builder.append("',correoElectronico:'");
        builder.append(correoElectronico);
        builder.append("',fechaDeMacimiento:'");
        builder.append(fechaDeMacimiento);
        builder.append("',celular:'");
        builder.append(celular);
        builder.append("',imagenDePerfil:'");
        builder.append(Arrays.toString(imagenDePerfil));
        builder.append("'}");
        return builder.toString();
    }

}
