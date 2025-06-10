package com.api.spring.boot.reportes.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ReporteDTO {
    private Integer idVenta;
    private LocalDate fechaVenta;
    private BigDecimal total;
    private Integer idVendedor;

    // Constructor
    public ReporteDTO(Integer idVenta, LocalDate fechaVenta, BigDecimal total, Integer idVendedor) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.idVendedor = idVendedor;
    }

    // Getters y setters
}
