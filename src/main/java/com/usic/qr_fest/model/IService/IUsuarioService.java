package com.usic.qr_fest.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Entity.Usuario;

@Service
public interface IUsuarioService {
    public List<Usuario> findAll();
    public void save(Usuario usuario);
    public Usuario findOne(Long idUsuario);
    public void delete(Long idUsuario);
    public Usuario findByUsername(String username);
    public Usuario getUsuarioPassword(String username, String password);
}
