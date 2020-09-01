package com.fm.modules.models;

import java.util.Arrays;

public class Restaurante {

    private Long restauranteId;
    private Departamento departamento;
    private String username;
    private String password;
    private String horarioDeApertura;
    private String horarioDeCierre;
    private String tiempoEstimadoDeEntrega;
    private double descuento;
    private String representante;
    private String numeroDeContacto;
    private double comision;
    private Integer cargosExtra;
    private Byte[] imagenDePortada;
    private Byte[] logoDeRestaurante;
    private String nit;
    private String correo;

    public Restaurante() {
    }

    public Restaurante(Long restauranteId, Departamento departamento, String username, String password,
                       String horarioDeApertura, String horarioDeCierre, String tiempoEstimadoDeEntrega, double descuento,
                       String representante, String numeroDeContacto, double comision, Integer cargosExtra, Byte[] imagenDePortada,
                       Byte[] logoDeRestaurante, String nit, String correo) {
        this.restauranteId = restauranteId;
        this.departamento = departamento;
        this.username = username;
        this.password = password;
        this.horarioDeApertura = horarioDeApertura;
        this.horarioDeCierre = horarioDeCierre;
        this.tiempoEstimadoDeEntrega = tiempoEstimadoDeEntrega;
        this.descuento = descuento;
        this.representante = representante;
        this.numeroDeContacto = numeroDeContacto;
        this.comision = comision;
        this.cargosExtra = cargosExtra;
        this.imagenDePortada = imagenDePortada;
        this.logoDeRestaurante = logoDeRestaurante;
        this.nit = nit;
        this.correo = correo;
    }

    public Long getRestautanteId() {
        return restauranteId;
    }

    public void setRestautanteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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

    public String getHorarioDeApertura() {
        return horarioDeApertura;
    }

    public void setHorarioDeApertura(String horarioDeApertura) {
        this.horarioDeApertura = horarioDeApertura;
    }

    public String getHorarioDeCierre() {
        return horarioDeCierre;
    }

    public void setHorarioDeCierre(String horarioDeCierre) {
        this.horarioDeCierre = horarioDeCierre;
    }

    public String getTiempoEstimadoDeEntrega() {
        return tiempoEstimadoDeEntrega;
    }

    public void setTiempoEstimadoDeEntrega(String tiempoEstimadoDeEntrega) {
        this.tiempoEstimadoDeEntrega = tiempoEstimadoDeEntrega;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getNumeroDeContacto() {
        return numeroDeContacto;
    }

    public void setNumeroDeContacto(String numeroDeContacto) {
        this.numeroDeContacto = numeroDeContacto;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public int getCargosExtra() {
        return cargosExtra;
    }

    public void setCargosExtra(Integer cargosExtra) {
        this.cargosExtra = cargosExtra;
    }

    public Byte[] getImagenDePortada() {
        return imagenDePortada;
    }

    public void setImagenDePortada(Byte[] imagenDePortada) {
        this.imagenDePortada = imagenDePortada;
    }

    public Byte[] getLogoDeRestaurante() {
        return logoDeRestaurante;
    }

    public void setLogoDeRestaurante(Byte[] logoDeRestaurante) {
        this.logoDeRestaurante = logoDeRestaurante;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{restautanteId:'");
        builder.append(restauranteId);
        builder.append("',departamentos:'");
        builder.append(departamento);
        builder.append("',username:'");
        builder.append(username);
        builder.append("',password:'");
        builder.append(password);
        builder.append("',horarioDeApertura:'");
        builder.append(horarioDeApertura);
        builder.append("',horarioDeCierre:'");
        builder.append(horarioDeCierre);
        builder.append("',tiempoEstimadoDeEntrega:'");
        builder.append(tiempoEstimadoDeEntrega);
        builder.append("',descuento:'");
        builder.append(descuento);
        builder.append("',representante:'");
        builder.append(representante);
        builder.append("',numeroDeContacto:'");
        builder.append(numeroDeContacto);
        builder.append("',comision:'");
        builder.append(comision);
        builder.append("',cargosExtra:'");
        builder.append(cargosExtra);
        builder.append("',imagenDePortada:'");
        builder.append(Arrays.toString(imagenDePortada));
        builder.append("',logoDeRestaurante:'");
        builder.append(Arrays.toString(logoDeRestaurante));
        builder.append("',nit:'");
        builder.append(nit);
        builder.append("',correo:'");
        builder.append(correo);
        builder.append("'}");
        return builder.toString();
    }

}
