package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.qr_fest.model.Entity.Titulo;

public interface ITituloDao extends JpaRepository<Titulo, Long> {
    Titulo findByTitulo(String titulo);
}
