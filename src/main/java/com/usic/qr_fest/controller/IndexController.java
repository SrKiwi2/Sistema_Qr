package com.usic.qr_fest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.usic.qr_fest.anotaciones.ValidarUsuarioAutenticado;
import com.usic.qr_fest.model.Entity.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

    @ValidarUsuarioAutenticado
    @GetMapping(value = "/vista_administrador")
    public String VistaAdministrador() {

        return "vista_admin";
    }

    @ValidarUsuarioAutenticado
    @GetMapping(value = "/escaneo")
    public String vistaEscaneoQr(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) return "redirect:/form-login";
        model.addAttribute("usuario", u);
        model.addAttribute("persona", session.getAttribute("persona"));
        model.addAttribute("rol", session.getAttribute("nombre_rol"));
        return "vista_escaneo";
    }
}
