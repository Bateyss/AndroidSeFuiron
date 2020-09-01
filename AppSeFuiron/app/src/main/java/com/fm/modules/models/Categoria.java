package com.fm.modules.models;

public class Categoria {

    private long categoriaId;
    private String nombreCategoria;

    public Categoria() {
    }

    public Categoria(long categoriaId, String nombreCategoria) {
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
    }

    public long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{categoriaId:'");
        builder.append(categoriaId);
        builder.append("',nombreCategoria:'");
        builder.append(nombreCategoria);
        builder.append("'}");
        return builder.toString();
    }

}
