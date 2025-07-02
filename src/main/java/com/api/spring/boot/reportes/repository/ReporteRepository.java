package com.api.spring.boot.reportes.repository;

import com.api.spring.boot.reportes.model.Boleta;
import com.api.spring.boot.reportes.model.ReporteDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReporteRepository extends Repository<Boleta, Long> {

  // Método para obtener ventas por rango de fechas
    @Query("SELECT new com.api.spring.boot.reportes.model.ReporteDTO(b.idVenta, b.fechaVenta, b.total, b.idVendedor) " +
       "FROM Boleta b WHERE b.fechaVenta BETWEEN :inicio AND :fin")
List<ReporteDTO> obtenerVentasPorFecha(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

}