package com.api.spring.boot.reportes.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/**
 * DTO para Venta - usado para integración con la API de Ventas
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VentaDTO extends RepresentationModel<VentaDTO> {
    
    private Long id_venta;
    private Long id_cliente;
    private Long id_vendedor;
    private LocalDate fechaVenta;
    private Double total;
    private Long id_metodopago;

    // Constructor vacío
    public VentaDTO() {}

    // Constructor con parámetros
    public VentaDTO(Long id_venta, Long id_cliente, Long id_vendedor, LocalDate fechaVenta, Double total, Long id_metodopago) {
        this.id_venta = id_venta;
        this.id_cliente = id_cliente;
        this.id_vendedor = id_vendedor;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.id_metodopago = id_metodopago;
    }

    // Getters y setters
    public Long getId_venta() {
        return id_venta;
    }

    public void setId_venta(Long id_venta) {
        this.id_venta = id_venta;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Long getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(Long id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getId_metodopago() {
        return id_metodopago;
    }

    public void setId_metodopago(Long id_metodopago) {
        this.id_metodopago = id_metodopago;
    }
} 