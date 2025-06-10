package com.api.spring.boot.reportes.service;


import com.api.spring.boot.reportes.model.ReporteDTO;
import com.api.spring.boot.reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    public List<ReporteDTO> ventasPorFecha(LocalDate inicio, LocalDate fin) {
        return repository.obtenerVentasPorFecha(inicio, fin);
    }
}

