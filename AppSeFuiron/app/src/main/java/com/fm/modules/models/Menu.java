package com.fm.modules.models;

public class Menu {

    private long menuId;
    private Restaurante restaurante;
    private String nombreMenu;
    private int orden;

    public Menu() {
    }

    public Menu(long menuId, Restaurante restaurante, String nombreMenu, int orden) {
        this.menuId = menuId;
        this.restaurante = restaurante;
        this.nombreMenu = nombreMenu;
        this.orden = orden;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
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
        builder.append("{menuId:'");
        builder.append(menuId);
        builder.append("',restaurante:'");
        builder.append(restaurante);
        builder.append("',nombreMenu:'");
        builder.append(nombreMenu);
        builder.append("',orden:'");
        builder.append(orden);
        builder.append("'}");
        return builder.toString();
    }

}
