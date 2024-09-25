package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.qr_fest.model.Entity.Rol;

public interface IRolDao extends JpaRepository<Rol, Long> {
    Rol findByNombreRol(String nombreRol);    
}
