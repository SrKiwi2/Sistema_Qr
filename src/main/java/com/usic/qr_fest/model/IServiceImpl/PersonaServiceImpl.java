package com.usic.qr_fest.model.IServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Dao.IPersonaDao;
import com.usic.qr_fest.model.Entity.Persona;
import com.usic.qr_fest.model.IService.IPersonaService;

@Service
public class PersonaServiceImpl implements IPersonaService{

    @Autowired
    private IPersonaDao personaDao;

    @Override
    public List<Persona> findAll() {
        return personaDao.findAll();
    }

    @Override
    public void save(Persona persona) {
        personaDao.save(persona);
    }

    @Override
    public Persona findOne(Long idPersona) {
        return personaDao.findById(idPersona).orElse(null);
    }

    @Override
    public void delete(Long idPersona) {
        personaDao.deleteById(idPersona);
    }

    @Override
    public Persona findByCi(String ci) {
        return personaDao.findByCi(ci);
    }
    
}
