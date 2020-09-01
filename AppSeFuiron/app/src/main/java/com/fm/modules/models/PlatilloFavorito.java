package com.fm.modules.models;

public class PlatilloFavorito {


    private long platilloFavoritoId;
    private Platillo platillos;
    private Usuario usuario;

    public PlatilloFavorito() {
    }

    public PlatilloFavorito(long platilloFavoritoId, Platillo platillos, Usuario usuario) {
        this.platilloFavoritoId = platilloFavoritoId;
        this.platillos = platillos;
        this.usuario = usuario;
    }

    public long getPlatilloFavoritoId() {
        return platilloFavoritoId;
    }

    public void setPlatilloFavoritoId(long platilloFavoritoId) {
        this.platilloFavoritoId = platilloFavoritoId;
    }

    public Platillo getPlatillos() {
        return platillos;
    }

    public void setPlatillos(Platillo platillos) {
        this.platillos = platillos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{platilloFavoritoId:'");
        builder.append(platilloFavoritoId);
        builder.append("',platillos:'");
        builder.append(platillos);
        builder.append("',usuarios:'");
        builder.append(usuario);
        builder.append("'}");
        return builder.toString();
    }

}
