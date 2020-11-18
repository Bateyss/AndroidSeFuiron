package com.fm.modules.app.carrito;

public class HabiliarAddMap {
    private Long id;
    private boolean habilitar;

    public HabiliarAddMap() {
    }

    public HabiliarAddMap(Long id, boolean habilitar) {
        this.id = id;
        this.habilitar = habilitar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHabilitar() {
        return habilitar;
    }

    public void setHabilitar(boolean habilitar) {
        this.habilitar = habilitar;
    }
}
