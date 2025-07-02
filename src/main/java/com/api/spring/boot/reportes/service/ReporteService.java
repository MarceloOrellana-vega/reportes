package com.api.spring.boot.reportes.service;

import com.api.spring.boot.reportes.model.ReporteDTO;
import com.api.spring.boot.reportes.repository.ReporteRepository;
import com.api.spring.boot.reportes.client.VentaClient;
import com.api.spring.boot.reportes.client.DetalleVentaClient;
import com.api.spring.boot.reportes.dto.VentaDTO;
import com.api.spring.boot.reportes.dto.DetalleVentaDTO;
import com.api.spring.boot.reportes.dto.ReporteIntegradoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    @Autowired
    private VentaClient ventaClient;

    @Autowired
    private DetalleVentaClient detalleVentaClient;

    /**
     * Obtiene ventas por fecha usando datos locales
     */
    public List<ReporteDTO> ventasPorFecha(LocalDate inicio, LocalDate fin) {
        return repository.obtenerVentasPorFecha(inicio, fin);
    }

    /**
     * Obtiene ventas por fecha desde la API de Ventas
     */
    public Mono<List<VentaDTO>> ventasPorFechaDesdeAPI(LocalDate inicio, LocalDate fin) {
        return ventaClient.obtenerTodasLasVentas()
            .map(ventas -> ventas.stream()
                .filter(venta -> !venta.getFechaVenta().isBefore(inicio) && !venta.getFechaVenta().isAfter(fin))
                .collect(Collectors.toList()));
    }

    /**
     * Obtiene todas las ventas desde la API de Ventas
     */
    public Mono<List<VentaDTO>> obtenerTodasLasVentas() {
        return ventaClient.obtenerTodasLasVentas();
    }

    /**
     * Obtiene una venta específica con sus detalles
     */
    public Mono<ReporteIntegradoDTO> obtenerVentaConDetalles(Long idVenta) {
        return ventaClient.obtenerVentaPorId(idVenta)
            .flatMap(venta -> {
                ReporteIntegradoDTO reporte = new ReporteIntegradoDTO(
                    venta.getId_venta(),
                    venta.getFechaVenta(),
                    venta.getTotal(),
                    venta.getId_cliente(),
                    venta.getId_vendedor(),
                    venta.getId_metodopago()
                );

                return detalleVentaClient.obtenerDetallesPorVenta(idVenta.intValue())
                    .map(detalles -> {
                        reporte.setDetalles(detalles);
                        reporte.calcularEstadisticas();
                        return reporte;
                    });
            });
    }

    /**
     * Obtiene estadísticas combinadas de todas las APIs
     */
    public Mono<Object> obtenerEstadisticasIntegradas() {
        return Mono.zip(
            ventaClient.obtenerEstadisticas(),
            detalleVentaClient.obtenerEstadisticas()
        ).map(tuple -> {
            // Crear objeto con estadísticas combinadas
            return new Object() {
                public final Object estadisticasVentas = tuple.getT1();
                public final Object estadisticasDetalles = tuple.getT2();
                public final String mensaje = "Estadísticas combinadas de Ventas y Detalle Ventas";
            };
        });
    }

    /**
     * Obtiene reportes integrados por rango de fechas
     */
    public Mono<List<ReporteIntegradoDTO>> obtenerReportesIntegradosPorFecha(LocalDate inicio, LocalDate fin) {
        return ventaClient.obtenerTodasLasVentas()
            .flatMap(ventas -> {
                List<VentaDTO> ventasFiltradas = ventas.stream()
                    .filter(venta -> !venta.getFechaVenta().isBefore(inicio) && !venta.getFechaVenta().isAfter(fin))
                    .collect(Collectors.toList());

                List<Mono<ReporteIntegradoDTO>> reportes = ventasFiltradas.stream()
                    .map(venta -> obtenerVentaConDetalles(venta.getId_venta()))
                    .collect(Collectors.toList());

                return Mono.zip(reportes, objects -> {
                    List<ReporteIntegradoDTO> result = new java.util.ArrayList<>();
                    for (Object obj : objects) {
                        result.add((ReporteIntegradoDTO) obj);
                    }
                    return result;
                });
            });
    }
}

