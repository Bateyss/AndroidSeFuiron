package com.fm.modules.models;

public class Pais {

    private long paisId;
	private String nombrePais;

	public Pais() {
	}

	public Pais(long paisId, String nombrePais) {
		this.paisId = paisId;
		this.nombrePais = nombrePais;
	}

    public long getPaisId() {
        return paisId;
    }

    public void setPaisId(long paisId) {
        this.paisId = paisId;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{paisId:'");
        builder.append(paisId);
        builder.append("',nombrePais:'");
        builder.append(nombrePais);
        builder.append("'}");
        return builder.toString();
    }


}
