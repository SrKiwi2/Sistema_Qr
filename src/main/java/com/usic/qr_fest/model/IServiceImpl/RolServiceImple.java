package com.usic.qr_fest.model.IServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Dao.IRolDao;
import com.usic.qr_fest.model.Entity.Rol;
import com.usic.qr_fest.model.IService.IRolService;

@Service
public class RolServiceImple implements IRolService {
    
    @Autowired
    private IRolDao rolDao;

    @Override
    public List<Rol> findAll() {
        return rolDao.findAll();
    }

    @Override
    public void save(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    public Rol findOne(Long idRol) {
        return rolDao.findById(idRol).orElse(null);
    }

    @Override
    public void delete(Long idRol) {
        rolDao.deleteById(idRol);
    }

    @Override
    public Rol findByNombreRol(String nombreRol) {
        return rolDao.findByNombreRol(nombreRol);
    }
}
