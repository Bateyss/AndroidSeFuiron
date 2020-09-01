package com.fm.modules.models;

import java.util.Arrays;

public class Platillo {

    private long platilloId;
    private Menu menu;
    private String nombre;
    private double precioBase;
    private byte[] imagen;
    private String descripcion;
    private int orden;

    public Platillo() {
    }

    public Platillo(long platilloId, Menu menu, String nombre, double precioBase, byte[] imagen, String descripcion,
                    int orden) {
        this.platilloId = platilloId;
        this.menu = menu;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public long getPlatilloId() {
        return platilloId;
    }

    public void setPlatilloId(long platilloId) {
        this.platilloId = platilloId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        builder.append("{platilloId:'");
        builder.append(platilloId);
        builder.append("',menus:'");
        builder.append(menu);
        builder.append("',nombre:'");
        builder.append(nombre);
        builder.append("',precioBase:'");
        builder.append(precioBase);
        builder.append("',imagen:'");
        builder.append(Arrays.toString(imagen));
        builder.append("',descripcion:'");
        builder.append(descripcion);
        builder.append("',orden:'");
        builder.append(orden);
        builder.append("'}");
        return builder.toString();
    }

}
