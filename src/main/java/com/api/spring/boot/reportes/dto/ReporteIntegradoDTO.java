package com.api.spring.boot.reportes.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para reportes integrados que combina datos de las APIs de Reportes, Ventas y Detalle Ventas
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReporteIntegradoDTO extends RepresentationModel<ReporteIntegradoDTO> {
    
    // Datos de la venta
    private Long idVenta;
    private LocalDate fechaVenta;
    private Double totalVenta;
    private Long idCliente;
    private Long idVendedor;
    private Long idMetodoPago;
    
    // Detalles de la venta
    private List<DetalleVentaDTO> detalles;
    private Integer cantidadProductos;
    private BigDecimal subtotalDetalles;
    
    // Estadísticas calculadas
    private Double promedioPorProducto;
    private String estadoVenta; // "Completa", "Pendiente", etc.

    // Constructor vacío
    public ReporteIntegradoDTO() {}

    // Constructor con datos básicos
    public ReporteIntegradoDTO(Long idVenta, LocalDate fechaVenta, Double totalVenta, 
                              Long idCliente, Long idVendedor, Long idMetodoPago) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.idMetodoPago = idMetodoPago;
        this.detalles = List.of();
        this.cantidadProductos = 0;
        this.subtotalDetalles = BigDecimal.ZERO;
        this.promedioPorProducto = 0.0;
        this.estadoVenta = "Completa";
    }

    // Método para calcular estadísticas basadas en los detalles
    public void calcularEstadisticas() {
        if (detalles != null && !detalles.isEmpty()) {
            this.cantidadProductos = detalles.size();
            this.subtotalDetalles = detalles.stream()
                .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            double totalDetalles = this.subtotalDetalles.doubleValue();
            this.promedioPorProducto = cantidadProductos > 0 ? totalDetalles / cantidadProductos : 0.0;
        } else {
            this.cantidadProductos = 0;
            this.subtotalDetalles = BigDecimal.ZERO;
            this.promedioPorProducto = 0.0;
        }
    }
} 