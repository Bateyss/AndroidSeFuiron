package com.fm.modules.entities;

public class RespuestaOpcionSubMenuPorPlatillo {

    private String submenuid;
    private String titulo;
    private boolean menuCobrado;
    private int cobrarAPartirDe;
    private int maximoOpcionesAEscoger;
    private int minimoOpcionesAEscoger;
    private String nombre;
    private double precio;
    private boolean seleccionadoPorDefecto;

    public RespuestaOpcionSubMenuPorPlatillo() {
    }

    public RespuestaOpcionSubMenuPorPlatillo(String submenuid, String titulo, boolean menuCobrado, int cobrarAPartirDe,
                                             int maximoOpcionesAEscoger, int minimoOpcionesAEscoger, String nombre, double precio,
                                             boolean seleccionadoPorDefecto) {
        super();
        this.submenuid = submenuid;
        this.titulo = titulo;
        this.menuCobrado = menuCobrado;
        this.cobrarAPartirDe = cobrarAPartirDe;
        this.maximoOpcionesAEscoger = maximoOpcionesAEscoger;
        this.minimoOpcionesAEscoger = minimoOpcionesAEscoger;
        this.nombre = nombre;
        this.precio = precio;
        this.seleccionadoPorDefecto = seleccionadoPorDefecto;
    }

    public String getSubmenuid() {
        return submenuid;
    }

    public void setSubmenuid(String submenuid) {
        this.submenuid = submenuid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isMenuCobrado() {
        return menuCobrado;
    }

    public void setMenuCobrado(boolean menuCobrado) {
        this.menuCobrado = menuCobrado;
    }

    public int getCobrarAPartirDe() {
        return cobrarAPartirDe;
    }

    public void setCobrarAPartirDe(int cobrarAPartirDe) {
        this.cobrarAPartirDe = cobrarAPartirDe;
    }

    public int getMaximoOpcionesAEscoger() {
        return maximoOpcionesAEscoger;
    }

    public void setMaximoOpcionesAEscoger(int maximoOpcionesAEscoger) {
        this.maximoOpcionesAEscoger = maximoOpcionesAEscoger;
    }

    public int getMinimoOpcionesAEscoger() {
        return minimoOpcionesAEscoger;
    }

    public void setMinimoOpcionesAEscoger(int minimoOpcionesAEscoger) {
        this.minimoOpcionesAEscoger = minimoOpcionesAEscoger;
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

    public boolean isSeleccionadoPorDefecto() {
        return seleccionadoPorDefecto;
    }

    public void setSeleccionadoPorDefecto(boolean seleccionadoPorDefecto) {
        this.seleccionadoPorDefecto = seleccionadoPorDefecto;
    }

    @Override
    public String toString() {
        return "RespuesOpcionesSubMenuPorPlatillo [submenuid=" + submenuid + ", titulo=" + titulo + ", menuCobrado="
                + menuCobrado + ", cobrarAPartirDe=" + cobrarAPartirDe + ", maximoOpcionesAEscoger="
                + maximoOpcionesAEscoger + ", minimoOpcionesAEscoger=" + minimoOpcionesAEscoger + ", nombre=" + nombre
                + ", precio=" + precio + ", seleccionadoPorDefecto=" + seleccionadoPorDefecto + "]";
    }
}
