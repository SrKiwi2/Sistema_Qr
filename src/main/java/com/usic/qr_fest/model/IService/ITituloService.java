package com.usic.qr_fest.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Entity.Titulo;

@Service
public interface ITituloService {
    public List<Titulo> findAll();
    public void save(Titulo titulo);
    public Titulo findOne(Long idTitulo);
    public void delete(Long idTitulo);
    public Titulo findByTitulo(String titulo);
}
