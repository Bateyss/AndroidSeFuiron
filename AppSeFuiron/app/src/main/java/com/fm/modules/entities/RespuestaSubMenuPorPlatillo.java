package com.fm.modules.entities;

public class RespuestaSubMenuPorPlatillo {
    private String submenuid;
    private String titulo;
    private boolean menuCobrado;
    private int cobrarAPartirDe;
    private int maximoOpcionesAEscoger;
    private int minimoOpcionesAEscoger;

    public RespuestaSubMenuPorPlatillo() {
    }

    public RespuestaSubMenuPorPlatillo(String submenuid, String titulo, boolean menuCobrado, int cobrarAPartirDe,
                                     int maximoOpcionesAEscoger, int minimoOpcionesAEscoger) {
        super();
        this.submenuid = submenuid;
        this.titulo = titulo;
        this.menuCobrado = menuCobrado;
        this.cobrarAPartirDe = cobrarAPartirDe;
        this.maximoOpcionesAEscoger = maximoOpcionesAEscoger;
        this.minimoOpcionesAEscoger = minimoOpcionesAEscoger;
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

    @Override
    public String toString() {
        return "RespuesSubmenuPorPlatillo [submenuid=" + submenuid + ", titulo=" + titulo + ", menuCobrado="
                + menuCobrado + ", cobrarAPartirDe=" + cobrarAPartirDe + ", maximoOpcionesAEscoger="
                + maximoOpcionesAEscoger + ", minimoOpcionesAEscoger=" + minimoOpcionesAEscoger + "]";
    }
}
