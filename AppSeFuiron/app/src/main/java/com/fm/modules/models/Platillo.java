package com.fm.modules.models;

import java.util.Arrays;

public class Platillo {

    private Long platilloId;
    private Menu menu;
    private String nombre;
    private double precioBase;
    private Long imagen;
    private String descripcion;
    private int orden;
    private int disponible;

    public Platillo() {
    }

    public Platillo(Long platilloId, Menu menu, String nombre, double precioBase, Long imagen, String descripcion, int orden, int disponible) {
        this.platilloId = platilloId;
        this.menu = menu;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.orden = orden;
        this.disponible = disponible;
    }

    public Long getPlatilloId() {
        return platilloId;
    }

    public void setPlatilloId(Long platilloId) {
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

    public Long getImagen() {
        return imagen;
    }

    public void setImagen(Long imagen) {
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

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Platillo{");
        sb.append("platilloId=").append(platilloId);
        sb.append(", menu=").append(menu);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", precioBase=").append(precioBase);
        sb.append(", imagen=").append(imagen);
        sb.append(", descripcion='").append(descripcion).append('\'');
        sb.append(", orden=").append(orden);
        sb.append(", disponible=").append(disponible);
        sb.append('}');
        return sb.toString();
    }
}
