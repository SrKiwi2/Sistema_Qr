package com.usic.qr_fest.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Entity.Rol;

@Service
public interface IRolService {
    public List<Rol> findAll();
    public void save(Rol rol);
    public Rol findOne(Long idRol);
    public void delete(Long idRol);
    public Rol findByNombreRol(String nombreRol);
}
