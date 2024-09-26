package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.qr_fest.model.Entity.Entrada;

public interface IEntradaDao extends JpaRepository<Entrada, Long> {

    Entrada findByIdentificador(String identificador);

    Entrada findByCodigo(String codigo);
}
