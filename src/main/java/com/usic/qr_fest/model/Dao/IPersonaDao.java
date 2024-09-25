package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.qr_fest.model.Entity.Persona;

public interface IPersonaDao extends JpaRepository<Persona, Long> {
    Persona findByCi(String ci);
}
