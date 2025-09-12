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
    public String vistaGenerarQr(@RequestParam("codigoEncriptado") String token, Model model) {
        String identificador = normalizeBase64ForQuery(token);

        Entrada e = entradaService.findByIdentificador(identificador);
        if (e == null) {
            model.addAttribute("variable", "LA ENTRADA NO EXISTE, POSIBLE FALSIFICACION");
            model.addAttribute("variable2", "3");
            return "/camara_qr";
        }

        if ("NO APROBADO".equals(e.getEstado())) {
            e.setEstado("APROBADO");
            e.setFecha_aprobado(new Date());
            entradaService.save(e);
            model.addAttribute("variable", "ENTRADA APROBADA");
            model.addAttribute("variable2", "1");
        } else if ("APROBADO".equals(e.getEstado())) {
            model.addAttribute("variable", "LA ENTRADA YA FUE APROBADA");
            model.addAttribute("variable2", "2");
        } else if ("USADO".equals(e.getEstado())) {
            model.addAttribute("variable", "LA ENTRADA YA FUE USADA");
            model.addAttribute("variable2", "4");
        } else {
            model.addAttribute("variable", "ESTADO: " + e.getEstado());
            model.addAttribute("variable2", "0");
        }
        return "/camara_qr";
    }

    // Repara espacios→'+' y agrega padding si falta (Base64 estándar)
    private String normalizeBase64ForQuery(String s) {
        if (s == null) return null;
        String v = s.trim().replace(' ', '+');     // por el + → espacio en query-string
        int m = v.length() % 4;                    // padding si falta
        if (m != 0) v = v + "====".substring(m);
        return v;
    }
}
