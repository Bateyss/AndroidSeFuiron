package com.fm.modules.models;

public class PlatillosNames {

    private Long idPlatillo;
    private String nombre;

    public PlatillosNames() {
    }

    public PlatillosNames(Long idPlatillo, String nombre) {
        this.idPlatillo = idPlatillo;
        this.nombre = nombre;
    }

    public Long getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(Long idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
