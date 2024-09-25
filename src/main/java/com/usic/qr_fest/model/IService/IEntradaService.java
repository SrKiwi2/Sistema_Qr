package com.usic.qr_fest.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Entity.Entrada;

@Service
public interface IEntradaService {
    
    public List<Entrada> findAll();
    public void save(Entrada entrada);
    public Entrada findOne(Long idEntrada);
    public void delete(Long idEntrada);
    public Entrada findByIdentificador(String identificador);
}
