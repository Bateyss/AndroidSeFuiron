package com.fm.modules.entities;

public class RespuestaPlatilloPorMenu {

    private String platilloId;
    private String nombre;
    private String precioBase;
    private byte[] imagen;
    private String descripcion;
    private String orden;

    public RespuestaPlatilloPorMenu() {
    }

    public RespuestaPlatilloPorMenu(String platilloId, String nombre, String precioBase, byte[] imagen,
                                    String descripcion, String orden) {
        super();
        this.platilloId = platilloId;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public String getPlatilloId() {
        return platilloId;
    }

    public void setPlatilloId(String platilloId) {
        this.platilloId = platilloId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(String precioBase) {
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

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "RespuestaPlatilloPorMenu [platilloId=" + platilloId + ", nombre=" + nombre + ", precioBase="
                + precioBase + ", imagen=" + imagen + ", descripcion=" + descripcion + ", orden=" + orden + "]";
    }
}
