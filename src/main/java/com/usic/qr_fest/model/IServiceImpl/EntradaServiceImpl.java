package com.usic.qr_fest.model.IServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.qr_fest.model.Dao.IEntradaDao;
import com.usic.qr_fest.model.Entity.Entrada;
import com.usic.qr_fest.model.IService.IEntradaService;

@Service
public class EntradaServiceImpl implements IEntradaService{

    @Autowired
    private IEntradaDao entradaDao;

    @Override
    public List<Entrada> findAll() {
        return entradaDao.findAll();
    }

    @Override
    public void save(Entrada entrada) {
        entradaDao.save(entrada);
    }

    @Override
    public Entrada findOne(Long idEntrada) {
        return entradaDao.findById(idEntrada).orElse(null);
    }

    @Override
    public void delete(Long idEntrada) {
        entradaDao.deleteById(idEntrada);
    }

    @Override
    public Entrada findByIdentificador(String identificador) {
        return entradaDao.findByIdentificador(identificador);
    }

    @Override
    public Entrada findByCodigo(String codigo) {
        return entradaDao.findByCodigo(codigo);
    }

    @Override
    public String findEstadoByIdentificador(Long identificador) {
        return entradaDao.findEstadoByIdentificador(identificador);
    }
}
