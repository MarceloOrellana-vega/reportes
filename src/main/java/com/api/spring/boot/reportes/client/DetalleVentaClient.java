package com.api.spring.boot.reportes.client;

import com.api.spring.boot.reportes.dto.DetalleVentaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Cliente para comunicarse con la API de Detalle Ventas
 */
@Component
public class DetalleVentaClient {

    private final WebClient webClient;

    public DetalleVentaClient(@Value("${api.detalleventa.base-url:http://localhost:8082}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Obtiene todos los detalles de venta
     */
    public Mono<List<DetalleVentaDTO>> obtenerTodosLosDetalles() {
        return webClient.get()
                .uri("/detalles")
                .retrieve()
                .bodyToMono(DetalleVentaResponse.class)
                .map(DetalleVentaResponse::getContent)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener detalles de venta: " + e.getMessage());
                    return Mono.just(List.of());
                });
    }

    /**
     * Obtiene un detalle específico por ID
     */
    public Mono<DetalleVentaDTO> obtenerDetallePorId(Integer id) {
        return webClient.get()
                .uri("/detalles/{id}", id)
                .retrieve()
                .bodyToMono(DetalleVentaDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener detalle " + id + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Obtiene detalles por ID de venta
     */
    public Mono<List<DetalleVentaDTO>> obtenerDetallesPorVenta(Integer idVenta) {
        return webClient.get()
                .uri("/detalles/venta/{idVenta}", idVenta)
                .retrieve()
                .bodyToMono(DetalleVentaResponse.class)
                .map(DetalleVentaResponse::getContent)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener detalles de la venta " + idVenta + ": " + e.getMessage());
                    return Mono.just(List.of());
                });
    }

    /**
     * Obtiene estadísticas de detalles de venta
     */
    public Mono<Object> obtenerEstadisticas() {
        return webClient.get()
                .uri("/detalles/stats")
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener estadísticas de detalles: " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Clase interna para manejar la respuesta paginada de la API de Detalle Ventas
     */
    public static class DetalleVentaResponse {
        private List<DetalleVentaDTO> content;

        public List<DetalleVentaDTO> getContent() {
            return content;
        }

        public void setContent(List<DetalleVentaDTO> content) {
            this.content = content;
        }
    }
} 