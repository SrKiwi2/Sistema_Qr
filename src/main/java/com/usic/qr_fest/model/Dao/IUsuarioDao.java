package com.usic.qr_fest.model.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.qr_fest.model.Entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
    Usuario findByUsername(String username);
    Usuario findByUsernameAndPassword(String username, String password);
}
