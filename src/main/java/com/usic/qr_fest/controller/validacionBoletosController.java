package com.usic.qr_fest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class validacionBoletosController {
    
    @GetMapping(value = "/pre-venta-mara")
    public String marasantos() {
        return "boletos/preventa/mara";
    }

    @GetMapping(value = "/pre-venta-joseca")
    public String joseca() {
        return "boletos/preventa/joseca";
    }

    @GetMapping(value = "/pre-venta-euphoria")
    public String euphoria() {
        return "boletos/preventa/euphoria";
    }

    @GetMapping(value = "/pre-venta-combo")
    public String combo() {
        return "boletos/preventa/combo";
    }

    @GetMapping(value = "/venta-mara")
    public String marasantos2() {
        return "boletos/venta/mara";
    }

    @GetMapping(value = "/venta-joseca")
    public String joseca2() {
        return "boletos/venta/joseca";
    }

    @GetMapping(value = "/venta-euphoria")
    public String euphoria2() {
        return "boletos/venta/euphoria";
    }

    @GetMapping(value = "/venta-combo")
    public String combo2() {
        return "boletos/venta/combo";
    }

    @GetMapping(value = "/fexpo_uapv1")
    public String fexpoUapV1() {
        return "boletos/venta/fexpo";
    }
}
