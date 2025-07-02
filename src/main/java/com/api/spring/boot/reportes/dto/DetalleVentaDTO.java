package com.api.spring.boot.reportes.dto;

import java.math.BigDecimal;

/**
 * DTO para DetalleVenta - usado para integración con la API de Detalle Ventas
 */
public class DetalleVentaDTO {
    
    private Integer idDetalle;
    private Integer idVenta;
    private Integer idProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    // Constructor vacío
    public DetalleVentaDTO() {}

    // Constructor con parámetros
    public DetalleVentaDTO(Integer idDetalle, Integer idVenta, Integer idProducto, Integer cantidad, BigDecimal precioUnitario) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y setters
    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
} 