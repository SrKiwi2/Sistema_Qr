package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.qr_fest.model.Entity.Entrada;

public interface IEntradaDao extends JpaRepository<Entrada, Long> {

    Entrada findByIdentificador(String identificador);

    Entrada findByCodigo(String codigo);

    @Query("SELECT e.Estado FROM Entrada e WHERE e.identificador = :identificador")
    String findEstadoByIdentificador(@Param("identificador") String identificador);

    @Query("SELECT e.idEntrada FROM Entrada e WHERE e.identificador = :identificador")
    Long findIdEntradaByIdentificador(@Param("identificador") String identificador);


}
