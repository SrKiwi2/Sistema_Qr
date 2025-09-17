package com.usic.qr_fest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class validacionBoletosController {
    
    @GetMapping(value = "/pre-venta-mara")
    public String VistaAdministrador() {
        return "boletos/mara";
    }
}
