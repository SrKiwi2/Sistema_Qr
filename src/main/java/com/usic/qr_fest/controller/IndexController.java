package com.usic.qr_fest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.usic.qr_fest.anotaciones.ValidarUsuarioAutenticado;

@Controller
public class IndexController {

    @ValidarUsuarioAutenticado
    @GetMapping(value = "/vista_administrador")
    public String VistaAdministrador() {

        return "vista_admin";
    }
}
