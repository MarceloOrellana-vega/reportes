package com.api.spring.boot.reportes.model;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Modelo para solicitar reportes por rango de fechas")
public class FechasRequest {
    @Schema(description = "Fecha de inicio en formato YYYY-MM-DD", example = "2024-01-01")
    private String inicio;
    
    @Schema(description = "Fecha de fin en formato YYYY-MM-DD", example = "2024-12-31")
    private String fin;
}
