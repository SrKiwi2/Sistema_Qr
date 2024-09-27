package com.usic.qr_fest.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.usic.qr_fest.model.Entity.Entrada;
import com.usic.qr_fest.model.IService.IEntradaService;

@Controller
public class CamaraQrController {

    @Autowired
    private IEntradaService entradaService;

    @GetMapping("/camara_qr")
    public String vistaGenerarQr(@RequestParam("codigoEncriptado") String codigoEncriptado, Model model) throws UnsupportedEncodingException {

        // Decodificar el código encriptado para manejar caracteres especiales
        

        String adjustedCodigoEncriptado = codigoEncriptado.replaceAll(" ", "+");

        if (entradaService.findIdEntradaByIdentificador(adjustedCodigoEncriptado) != null) {

            if (entradaService.findEstadoByIdentificador(adjustedCodigoEncriptado).equals("NO APROBADO")) {
                Entrada entrada = entradaService.findOne(entradaService.findIdEntradaByIdentificador(adjustedCodigoEncriptado));    
                entrada.setEstado("APROBADO");
                entrada.setFecha_aprobado(new Date());
                entradaService.save(entrada);
                model.addAttribute("variable", "ENTRADA APROBADA");
                model.addAttribute("variable2", "1");

            }else{
                model.addAttribute("variable", "LA ENTRADA YA FUE APROBADA");
                model.addAttribute("variable2", "2");
            }
        }else{
            model.addAttribute("variable", "LA ENTRADA NO EXISTE, POSIBLE FALSIFICACION");
            model.addAttribute("variable2", "3");
        }
        
        return "/camara_qr";
    }

}
