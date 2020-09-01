package com.fm.modules.models;

public class Municipio {

    private long municipioId;
    private Departamento departamento;
    private String nombreMunicipio;

    public Municipio() {
    }

    public Municipio(long municipioId, Departamento departamento, String nombreMunicipio) {
        this.municipioId = municipioId;
        this.departamento = departamento;
        this.nombreMunicipio = nombreMunicipio;
    }

    public long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(long municipioId) {
        this.municipioId = municipioId;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

}
