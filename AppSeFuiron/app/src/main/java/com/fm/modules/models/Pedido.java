package com.fm.modules.models;

import java.util.Date;

public class Pedido {

    private long pedidoId;
    private Restaurante restaurante;
    private Usuario usuario;
    private Driver driver;
    private int status;
    private String formaDePago;
    private double totalDePedido;
    private double totalEnRestautante;
    private double totalDeCargosExtra;
    private double totalEnRestautanteSinComision;
    private boolean pedidoPagado;
    private Date fechaOrdenado;
    private String tiempoPromedioEntrega;
    private boolean pedidoEntregado;
    private String notas;
    private String tiempoAdicional;
    private String direccion;

    public Pedido() {
    }

    public Pedido(long pedidoId, Restaurante restaurante, Usuario usuario, Driver driver, int status,
                  String formaDePago, double totalDePedido, double totalEnRestautante, double totalDeCargosExtra,
                  double totalEnRestautanteSinComision, boolean pedidoPagado, Date fechaOrdenado, String tiempoPromedioEntrega,
                  boolean pedidoEntregado, String notas, String tiempoAdicional, String direccion) {
        this.pedidoId = pedidoId;
        this.restaurante = restaurante;
        this.usuario = usuario;
        this.driver = driver;
        this.status = status;
        this.formaDePago = formaDePago;
        this.totalDePedido = totalDePedido;
        this.totalEnRestautante = totalEnRestautante;
        this.totalDeCargosExtra = totalDeCargosExtra;
        this.totalEnRestautanteSinComision = totalEnRestautanteSinComision;
        this.pedidoPagado = pedidoPagado;
        this.fechaOrdenado = fechaOrdenado;
        this.tiempoPromedioEntrega = tiempoPromedioEntrega;
        this.pedidoEntregado = pedidoEntregado;
        this.notas = notas;
        this.tiempoAdicional = tiempoAdicional;
        this.direccion = direccion;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurantes(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(String formaDePago) {
        this.formaDePago = formaDePago;
    }

    public double getTotalDePedido() {
        return totalDePedido;
    }

    public void setTotalDePedido(double totalDePedido) {
        this.totalDePedido = totalDePedido;
    }

    public double getTotalEnRestautante() {
        return totalEnRestautante;
    }

    public void setTotalEnRestautante(double totalEnRestautante) {
        this.totalEnRestautante = totalEnRestautante;
    }

    public double getTotalDeCargosExtra() {
        return totalDeCargosExtra;
    }

    public void setTotalDeCargosExtra(double totalDeCargosExtra) {
        this.totalDeCargosExtra = totalDeCargosExtra;
    }

    public double getTotalEnRestautanteSinComision() {
        return totalEnRestautanteSinComision;
    }

    public void setTotalEnRestautanteSinComision(double totalEnRestautanteSinComision) {
        this.totalEnRestautanteSinComision = totalEnRestautanteSinComision;
    }

    public boolean isPedidoPagado() {
        return pedidoPagado;
    }

    public void setPedidoPagado(boolean pedidoPagado) {
        this.pedidoPagado = pedidoPagado;
    }

    public Date getFechaOrdenado() {
        return fechaOrdenado;
    }

    public void setFechaOrdenado(Date fechaOrdenado) {
        this.fechaOrdenado = fechaOrdenado;
    }

    public String getTiempoPromedioEntrega() {
        return tiempoPromedioEntrega;
    }

    public void setTiempoPromedioEntrega(String tiempoPromedioEntrega) {
        this.tiempoPromedioEntrega = tiempoPromedioEntrega;
    }

    public boolean isPedidoEntregado() {
        return pedidoEntregado;
    }

    public void setPedidoEntregado(boolean pedidoEntregado) {
        this.pedidoEntregado = pedidoEntregado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getTiempoAdicional() {
        return tiempoAdicional;
    }

    public void setTiempoAdicional(String tiempoAdicional) {
        this.tiempoAdicional = tiempoAdicional;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{pedidoId:'");
        builder.append(pedidoId);
        builder.append("',restaurantes:'");
        builder.append(restaurante);
        builder.append("',usuarios:'");
        builder.append(usuario);
        builder.append("',drivers:'");
        builder.append(driver);
        builder.append("',status:'");
        builder.append(status);
        builder.append("',formaDePago:'");
        builder.append(formaDePago);
        builder.append("',totalDePedido:'");
        builder.append(totalDePedido);
        builder.append("',totalEnRestautante:'");
        builder.append(totalEnRestautante);
        builder.append("',totalDeCargosExtra:'");
        builder.append(totalDeCargosExtra);
        builder.append("',totalEnRestautanteSinComision:'");
        builder.append(totalEnRestautanteSinComision);
        builder.append("',pedidoPagado:'");
        builder.append(pedidoPagado);
        builder.append("',fechaOrdenado:'");
        builder.append(fechaOrdenado);
        builder.append("',tiempoPromedioEntrega:'");
        builder.append(tiempoPromedioEntrega);
        builder.append("',pedidoEntregado:'");
        builder.append(pedidoEntregado);
        builder.append("',notas:'");
        builder.append(notas);
        builder.append("',tiempoAdicional:'");
        builder.append(tiempoAdicional);
        builder.append("',direccion:'");
        builder.append(direccion);
        builder.append("'}");
        return builder.toString();
    }

}
