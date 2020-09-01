package com.fm.modules.models;

public class RestauranteDestacado {


    private long restauranteDestacadoId;
    private Restaurante restaurante;
    private String nombre;
    private byte[] imagen;
    private double montoMinimo;
    private byte[] logo;
    private int orden;

    public RestauranteDestacado() {
    }

    public RestauranteDestacado(long restauranteDestacadoId, Restaurante restaurante, String nombre, byte[] imagen,
                                double montoMinimo, byte[] logo, int orden) {
        this.restauranteDestacadoId = restauranteDestacadoId;
        this.restaurante = restaurante;
        this.nombre = nombre;
        this.imagen = imagen;
        this.montoMinimo = montoMinimo;
        this.logo = logo;
        this.orden = orden;
    }

    public long getRestauranteDestacadoId() {
        return restauranteDestacadoId;
    }

    public void setRestauranteDestacadoId(long restauranteDestacadoId) {
        this.restauranteDestacadoId = restauranteDestacadoId;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurantes(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public double getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(double montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{restauranteDestacadoId:'");
        builder.append(restauranteDestacadoId);
        builder.append("',restaurantes:'");
        builder.append(restaurante);
        builder.append("',nombre:'");
        builder.append(nombre);
        builder.append("',montoMinimo:'");
        builder.append(montoMinimo);
        builder.append("',orden:'");
        builder.append(orden);
        builder.append("'}");
        return builder.toString();
    }

}
