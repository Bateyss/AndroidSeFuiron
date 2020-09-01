package com.fm.modules.models;

public class PlatilloSeleccionado {

    private long platilloSeleccionadoId;
    private Platillo platillos;
    private Pedido pedido;
    private String nombre;
    private double precio;

    public PlatilloSeleccionado() {
    }

    public PlatilloSeleccionado(long platilloSeleccionadoId, Platillo platillos, Pedido pedido, String nombre,
                                double precio) {
        this.platilloSeleccionadoId = platilloSeleccionadoId;
        this.platillos = platillos;
        this.pedido = pedido;
        this.nombre = nombre;
        this.precio = precio;
    }

    public long getPlatilloSeleccionadoId() {
        return platilloSeleccionadoId;
    }

    public void setPlatilloSeleccionadoId(long platilloSeleccionadoId) {
        this.platilloSeleccionadoId = platilloSeleccionadoId;
    }

    public Platillo getPlatillos() {
        return platillos;
    }

    public void setPlatillos(Platillo platillos) {
        this.platillos = platillos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{platilloSeleccionadoId:'");
        builder.append(platilloSeleccionadoId);
        builder.append("',platillos:'");
        builder.append(platillos);
        builder.append("',pedidos:'");
        builder.append(pedido);
        builder.append("',nombre:'");
        builder.append(nombre);
        builder.append("',precio:'");
        builder.append(precio);
        builder.append("'}");
        return builder.toString();
    }

}
