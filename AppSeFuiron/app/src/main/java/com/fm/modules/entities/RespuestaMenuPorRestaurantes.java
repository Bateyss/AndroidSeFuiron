package com.fm.modules.entities;

public class RespuestaMenuPorRestaurantes {

    private String menuId;
    private String nombreMenu;
    private String orden;
    private String nombreRestaurante;

    public RespuestaMenuPorRestaurantes() {
    }

    public RespuestaMenuPorRestaurantes(String menuId, String nombreMenu, String orden, String nombreRestaurante) {
        super();
        this.menuId = menuId;
        this.nombreMenu = nombreMenu;
        this.orden = orden;
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    @Override
    public String toString() {
        return "RespuestaMenuPorRestaurantes [menuId=" + menuId + ", nombreMenu=" + nombreMenu + ", orden=" + orden
                + ", nombreRestaurante=" + nombreRestaurante + "]";
    }

}
