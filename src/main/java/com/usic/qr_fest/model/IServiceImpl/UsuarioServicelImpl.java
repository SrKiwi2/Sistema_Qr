package com.usic.qr_fest.model.IServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Dao.IUsuarioDao;
import com.usic.qr_fest.model.Entity.Usuario;
import com.usic.qr_fest.model.IService.IUsuarioService;

@Service
public class UsuarioServicelImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Override
    public void save(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    public Usuario findOne(Long idUsuario) {
        return usuarioDao.findById(idUsuario).orElse(null);
    }

    @Override
    public void delete(Long idUsuario) {
        usuarioDao.deleteById(idUsuario);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }

    @Override
    public Usuario getUsuarioPassword(String username, String password) {
        return usuarioDao.findByUsernameAndPassword(username, password);
    }
}
