package com.fm.modules.models;

public class PedidoDos {

    private Long pedidoId;
    private Restaurante restaurante;
    private Usuario usuario;
    private Driver drivers;
    private int status;
    private String formaDePago;
    private double totalDePedido;
    private double totalEnRestautante;
    private double totalDeCargosExtra;
    private double totalEnRestautanteSinComision;
    private boolean pedidoPagado;
    private String fechaOrdenado;
    private String tiempoPromedioEntrega;
    private boolean pedidoEntregado;
    private String notas;
    private String tiempoAdicional;
    private String direccion;

    public PedidoDos() {
    }

    public PedidoDos(Long pedidoId, Restaurante restaurante, Usuario usuario, Driver driver, int status,
                     String formaDePago, double totalDePedido, double totalEnRestautante, double totalDeCargosExtra,
                     double totalEnRestautanteSinComision, boolean pedidoPagado, String fechaOrdenado, String tiempoPromedioEntrega,
                     boolean pedidoEntregado, String notas, String tiempoAdicional, String direccion) {
        this.pedidoId = pedidoId;
        this.restaurante = restaurante;
        this.usuario = usuario;
        this.drivers = driver;
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

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Driver getDrivers() {
        return drivers;
    }

    public void setDrivers(Driver driver) {
        this.drivers = driver;
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

    public String getFechaOrdenado() {
        return fechaOrdenado;
    }

    public void setFechaOrdenado(String fechaOrdenado) {
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
        builder.append(drivers);
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
