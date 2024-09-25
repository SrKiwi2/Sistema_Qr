package com.usic.qr_fest.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Entity.Persona;

@Service
public interface IPersonaService {
    public List<Persona> findAll();
    public void save(Persona persona);
    public Persona findOne(Long idPersona);
    public void delete(Long idPersona);
    public Persona findByCi(String ci);
}
