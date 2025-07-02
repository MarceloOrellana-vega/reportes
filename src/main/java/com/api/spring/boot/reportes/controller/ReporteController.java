package com.api.spring.boot.reportes.controller;

import com.api.spring.boot.reportes.model.ReporteDTO;
import com.api.spring.boot.reportes.model.FechasRequest;
import com.api.spring.boot.reportes.service.ReporteService;
import com.api.spring.boot.reportes.dto.VentaDTO;
import com.api.spring.boot.reportes.dto.ReporteIntegradoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Importaciones  
@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "API para gestión de reportes de ventas con integración a APIs de Ventas y Detalle Ventas")
public class ReporteController {

    @Autowired
    private ReporteService service;

    // Método GET con HATEOAS y documentación Swagger
    @GetMapping("/ventas-por-fecha")
    @Operation(summary = "Obtener ventas por rango de fechas", 
               description = "Retorna una lista de ventas filtradas por fecha de inicio y fin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas encontradas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    
    public CollectionModel<ReporteDTO> obtenerVentasPorFechaGet(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", example = "2024-01-01")
            @RequestParam("inicio") String inicio,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", example = "2024-12-31")
            @RequestParam("fin") String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        List<ReporteDTO> ventas = service.ventasPorFecha(fechaInicio, fechaFin);
        
        // Agregar enlaces HATEOAS a cada venta
        List<ReporteDTO> ventasConEnlaces = ventas.stream()
            .map(venta -> {
                venta.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentasPorFechaGet(inicio, fin)).withRel("self"));
                venta.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentasPorFechaGet(inicio, fin)).withRel("ventas-por-fecha"));
                // Enlace al API Gateway
                venta.add(Link.of("http://localhost:8888/reportes/ventas-por-fecha", "api-gateway"));
                // Enlaces a las APIs integradas
                venta.add(Link.of("http://localhost:8888/ventas", "api-ventas"));
                venta.add(Link.of("http://localhost:8888/detalles", "api-detalle-ventas"));
                return venta;
            })
            .collect(Collectors.toList());
        
        CollectionModel<ReporteDTO> collectionModel = CollectionModel.of(ventasConEnlaces);
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerVentasPorFechaGet(inicio, fin)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerTodasLasVentas()).withRel("todas-las-ventas"));
        
        return collectionModel;
    }

    // Método POST con HATEOAS y documentación Swagger
    @PostMapping("/ventas-por-fecha")
    @Operation(summary = "Obtener ventas por rango de fechas (POST)", 
               description = "Retorna una lista de ventas filtradas por fecha usando POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas encontradas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<ReporteDTO> obtenerVentasPorFechaPost(
            @Parameter(description = "Rango de fechas para filtrar ventas")
            @RequestBody FechasRequest fechas) {
        
        LocalDate fechaInicio = LocalDate.parse(fechas.getInicio());
        LocalDate fechaFin = LocalDate.parse(fechas.getFin());
        List<ReporteDTO> ventas = service.ventasPorFecha(fechaInicio, fechaFin);
        
        // Agregar enlaces HATEOAS a cada venta
        List<ReporteDTO> ventasConEnlaces = ventas.stream()
            .map(venta -> {
                venta.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentasPorFechaPost(fechas)).withRel("self"));
                venta.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentasPorFechaGet(fechas.getInicio(), fechas.getFin())).withRel("get-ventas"));
                // Enlace al API Gateway
                venta.add(Link.of("http://localhost:8888/reportes/ventas-por-fecha", "api-gateway"));
                // Enlaces a las APIs integradas
                venta.add(Link.of("http://localhost:8888/ventas", "api-ventas"));
                venta.add(Link.of("http://localhost:8888/detalles", "api-detalle-ventas"));
                return venta;
            })
            .collect(Collectors.toList());
        
        CollectionModel<ReporteDTO> collectionModel = CollectionModel.of(ventasConEnlaces);
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerVentasPorFechaPost(fechas)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerTodasLasVentas()).withRel("todas-las-ventas"));
        
        return collectionModel;
    }

    // Nuevo endpoint para obtener todas las ventas
    @GetMapping("/todas-las-ventas")
    @Operation(summary = "Obtener todas las ventas", 
               description = "Retorna una lista de todas las ventas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<ReporteDTO> obtenerTodasLasVentas() {
        // Este método asume que tienes un servicio para obtener todas las ventas
        // Por ahora retornamos una lista vacía como ejemplo
        List<ReporteDTO> ventas = List.of(); 
        
        CollectionModel<ReporteDTO> collectionModel = CollectionModel.of(ventas);
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerTodasLasVentas()).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerVentasPorFechaGet("2024-01-01", "2024-12-31")).withRel("ventas-por-fecha"));
        
        return collectionModel;
    }

    //  Endpoint para obtener ventas desde la API de Ventas
    @GetMapping("/ventas-api")
    @Operation(summary = "Obtener ventas desde API de Ventas", 
               description = "Retorna ventas obtenidas directamente desde la API de Ventas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error de comunicación con API de Ventas")
    })
    public CollectionModel<VentaDTO> obtenerVentasDesdeAPI() {
        List<VentaDTO> ventas = service.obtenerTodasLasVentas().block();
        
        if (ventas == null) {
            ventas = List.of();
        }
        
        List<VentaDTO> ventasConEnlaces = ventas.stream()
            .map(venta -> {
                venta.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentaConDetalles(venta.getId_venta())).withRel("venta-con-detalles"));
                venta.add(Link.of("http://localhost:8888/ventas/" + venta.getId_venta(), "api-ventas"));
                venta.add(Link.of("http://localhost:8888/detalles/venta/" + venta.getId_venta(), "api-detalle-ventas"));
                return venta;
            })
            .collect(Collectors.toList());
        
        CollectionModel<VentaDTO> collectionModel = CollectionModel.of(ventasConEnlaces);
        collectionModel.add(linkTo(methodOn(ReporteController.class).obtenerVentasDesdeAPI()).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReporteController.class).obtenerEstadisticasIntegradas()).withRel("estadisticas-integradas"));
        
        return collectionModel;
    }

    // Endpoint para obtener una venta específica con sus detalles
    @GetMapping("/venta/{idVenta}/detalles")
    @Operation(summary = "Obtener venta con detalles", 
               description = "Retorna una venta específica con todos sus detalles de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta con detalles obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error de comunicación con APIs")
    })
    public EntityModel<ReporteIntegradoDTO> obtenerVentaConDetalles(
            @Parameter(description = "ID de la venta") @PathVariable Long idVenta) {
        
        ReporteIntegradoDTO reporte = service.obtenerVentaConDetalles(idVenta).block();
        
        if (reporte == null) {
            return null; // Manejar 404
        }
        
        EntityModel<ReporteIntegradoDTO> entityModel = EntityModel.of(reporte);
        entityModel.add(linkTo(methodOn(ReporteController.class).obtenerVentaConDetalles(idVenta)).withSelfRel());
        entityModel.add(Link.of("http://localhost:8888/ventas/" + idVenta, "api-ventas"));
        entityModel.add(Link.of("http://localhost:8888/detalles/venta/" + idVenta, "api-detalle-ventas"));
        entityModel.add(linkTo(methodOn(ReporteController.class).obtenerEstadisticasIntegradas()).withRel("estadisticas-integradas"));
        
        return entityModel;
    }

    //  Endpoint para reportes integrados por fecha
    @GetMapping("/integrado-por-fecha")
    @Operation(summary = "Reporte integrado por fecha", 
               description = "Retorna reportes completos con ventas y detalles por rango de fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes integrados obtenidos exitosamente"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
        @ApiResponse(responseCode = "500", description = "Error de comunicación con APIs")
    })
    public CollectionModel<ReporteIntegradoDTO> obtenerReportesIntegradosPorFecha(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", example = "2024-01-01")
            @RequestParam("inicio") String inicio,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", example = "2024-12-31")
            @RequestParam("fin") String fin) {
        
        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        
        List<ReporteIntegradoDTO> reportes = service.obtenerReportesIntegradosPorFecha(fechaInicio, fechaFin).block();
        
        if (reportes == null) {
            reportes = List.of();
        }
        
        List<ReporteIntegradoDTO> reportesConEnlaces = reportes.stream()
            .map(reporte -> {
                reporte.add(linkTo(methodOn(ReporteController.class)
                    .obtenerVentaConDetalles(reporte.getIdVenta())).withRel("venta-detalles"));
                reporte.add(Link.of("http://localhost:8888/ventas/" + reporte.getIdVenta(), "api-ventas"));
                reporte.add(Link.of("http://localhost:8888/detalles/venta/" + reporte.getIdVenta(), "api-detalle-ventas"));
                return reporte;
            })
            .collect(Collectors.toList());
        
        CollectionModel<ReporteIntegradoDTO> collectionModel = CollectionModel.of(reportesConEnlaces);
        collectionModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerReportesIntegradosPorFecha(inicio, fin)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ReporteController.class).obtenerEstadisticasIntegradas()).withRel("estadisticas-integradas"));
        
        return collectionModel;
    }

    // Endpoint para estadísticas integradas
    @GetMapping("/estadisticas-integradas")
    @Operation(summary = "Estadísticas integradas", 
               description = "Retorna estadísticas combinadas de las APIs de Ventas y Detalle Ventas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error de comunicación con APIs")
    })
    public EntityModel<Object> obtenerEstadisticasIntegradas() {
        Object estadisticas = service.obtenerEstadisticasIntegradas().block();
        
        EntityModel<Object> entityModel = EntityModel.of(estadisticas);
        entityModel.add(linkTo(methodOn(ReporteController.class).obtenerEstadisticasIntegradas()).withSelfRel());
        entityModel.add(Link.of("http://localhost:8888/ventas/stats", "api-ventas-stats"));
        entityModel.add(Link.of("http://localhost:8888/detalles/stats", "api-detalle-ventas-stats"));
        
        return entityModel;
    }

    // Endpoint de información de la API
    @GetMapping("/info")
    @Operation(summary = "Información de la API", 
               description = "Retorna información básica sobre la API de reportes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente")
    })
    public EntityModel<String> obtenerInfo() {
        String info = "API de Reportes - Versión 1.0\n" +
                     "Endpoints disponibles:\n" +
                     "- GET /reportes/ventas-por-fecha\n" +
                     "- POST /reportes/ventas-por-fecha\n" +
                     "- GET /reportes/todas-las-ventas\n" +
                     "- GET /reportes/ventas-api (NUEVO: desde API de Ventas)\n" +
                     "- GET /reportes/venta/{id}/detalles (NUEVO: venta con detalles)\n" +
                     "- GET /reportes/integrado-por-fecha (NUEVO: reportes integrados)\n" +
                     "- GET /reportes/estadisticas-integradas (NUEVO: estadísticas combinadas)\n" +
                     "- GET /reportes/info\n" +
                     "\nAPIs Integradas:\n" +
                     "- API Ventas: http://localhost:8181\n" +
                     "- API Detalle Ventas: http://localhost:8082\n" +
                     "- API Gateway: http://localhost:8888";
        
        EntityModel<String> entityModel = EntityModel.of(info);
        entityModel.add(linkTo(methodOn(ReporteController.class).obtenerInfo()).withSelfRel());
        entityModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerVentasPorFechaGet("2024-01-01", "2024-12-31")).withRel("ventas-por-fecha"));
        entityModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerVentasDesdeAPI()).withRel("ventas-api"));
        entityModel.add(linkTo(methodOn(ReporteController.class)
            .obtenerEstadisticasIntegradas()).withRel("estadisticas-integradas"));
        
        return entityModel;
    }
}
