package com.usic.qr_fest.model.IServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Dao.ITituloDao;
import com.usic.qr_fest.model.Entity.Titulo;
import com.usic.qr_fest.model.IService.ITituloService;

@Service
public class TituloServiceImpl implements ITituloService {
    @Autowired
    private ITituloDao tituloDao;

    @Override
    public List<Titulo> findAll() {
        return tituloDao.findAll();
    }

    @Override
    public void save(Titulo titulo) {
        tituloDao.save(titulo);
    }

    @Override
    public Titulo findOne(Long idTitulo) {
        return tituloDao.findById(idTitulo).orElse(null);
    }

    @Override
    public void delete(Long idTitulo) {
        tituloDao.deleteById(idTitulo);
    }

    @Override
    public Titulo findByTitulo(String titulo) {
        return tituloDao.findByTitulo(titulo);
    }
    
}
