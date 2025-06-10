package com.api.spring.boot.reportes.controller;

import com.api.spring.boot.reportes.model.ReporteDTO;
import com.api.spring.boot.reportes.model.FechasRequest;
import com.api.spring.boot.reportes.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    // Método GET
    @GetMapping("/ventas-por-fecha")
    public List<ReporteDTO> obtenerVentasPorFechaGet(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return service.ventasPorFecha(fechaInicio, fechaFin);
    }

    // Método POST
    @PostMapping("/ventas-por-fecha")
    public List<ReporteDTO> obtenerVentasPorFechaPost(@RequestBody FechasRequest fechas) {
        LocalDate fechaInicio = LocalDate.parse(fechas.getInicio());
        LocalDate fechaFin = LocalDate.parse(fechas.getFin());
        return service.ventasPorFecha(fechaInicio, fechaFin);
    }
}
