package com.api.spring.boot.reportes.client;

import com.api.spring.boot.reportes.dto.VentaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Cliente para comunicarse con la API de Ventas
 */
@Component
public class VentaClient {

    private final WebClient webClient;

    public VentaClient(@Value("${api.ventas.base-url:http://localhost:8181}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Obtiene todas las ventas de la API de Ventas
     */
    public Mono<List<VentaDTO>> obtenerTodasLasVentas() {
        return webClient.get()
                .uri("/ventas")
                .retrieve()
                .bodyToMono(VentaResponse.class)
                .map(VentaResponse::getContent)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener ventas: " + e.getMessage());
                    return Mono.just(List.of());
                });
    }

    /**
     * Obtiene una venta específica por ID
     */
    public Mono<VentaDTO> obtenerVentaPorId(Long id) {
        return webClient.get()
                .uri("/ventas/{id}", id)
                .retrieve()
                .bodyToMono(VentaDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener venta " + id + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Obtiene estadísticas de ventas
     */
    public Mono<Object> obtenerEstadisticas() {
        return webClient.get()
                .uri("/ventas/stats")
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener estadísticas: " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Obtiene ventas por cliente
     */
    public Mono<List<VentaDTO>> obtenerVentasPorCliente(Long idCliente) {
        return webClient.get()
                .uri("/ventas/cliente/{idCliente}", idCliente)
                .retrieve()
                .bodyToMono(VentaResponse.class)
                .map(VentaResponse::getContent)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error al obtener ventas del cliente " + idCliente + ": " + e.getMessage());
                    return Mono.just(List.of());
                });
    }

    /**
     * Clase interna para manejar la respuesta paginada de la API de Ventas
     */
    public static class VentaResponse {
        private List<VentaDTO> content;

        public List<VentaDTO> getContent() {
            return content;
        }

        public void setContent(List<VentaDTO> content) {
            this.content = content;
        }
    }
} 