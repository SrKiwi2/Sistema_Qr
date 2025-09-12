package com.usic.qr_fest.model.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.qr_fest.model.Entity.Entrada;

public interface IEntradaDao extends JpaRepository<Entrada, Long> {

    Entrada findByIdentificador(String identificador);

    Entrada findByCodigo(String codigo);

    // FIX: 'estado' en minúscula
    @Query("SELECT e.estado FROM Entrada e WHERE e.identificador = :identificador")
    String findEstadoByIdentificador(@Param("identificador") String identificador);

    @Query("SELECT e.idEntrada FROM Entrada e WHERE e.identificador = :identificador")
    Long findIdEntradaByIdentificador(@Param("identificador") String identificador);

    // FIX: quitar NULLS LAST (JPQL no lo soporta)
    @Query("SELECT e FROM Entrada e ORDER BY e.fecha_aprobado DESC")
    List<Entrada> findAllOrderByFechaAprobadoDesc();

    // --- OPCIONALES, SOLO SI QUIERES NULLS LAST REAL ---
    // Opción A: método nativo (PostgreSQL), respeta 'NULLS LAST'
    @Query(value = "SELECT * FROM entrada ORDER BY fecha_aprobado DESC NULLS LAST", nativeQuery = true)
    List<Entrada> findAllOrderByFechaAprobadoDescNullsLastNative();
    
}
