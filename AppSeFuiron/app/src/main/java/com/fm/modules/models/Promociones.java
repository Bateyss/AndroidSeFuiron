package com.fm.modules.models;

import java.math.BigDecimal;

public class Promociones {

    private Long promocionId;
    private String mensaje;
    private String fechaInicio;
    private String fechaFin;
    private String codigo;
    private BigDecimal porcentajeDescuento;

    public Promociones() {
    }

    public Promociones(Long promocionId, String mensaje, String fechaInicio, String fechaFin, String codigo, BigDecimal porcentajeDescuento) {
        this.promocionId = promocionId;
        this.mensaje = mensaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.codigo = codigo;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Long getPromocionId() {
        return promocionId;
    }

    public void setPromocionId(Long promocionId) {
        this.promocionId = promocionId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
