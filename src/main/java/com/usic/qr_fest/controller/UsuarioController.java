package com.usic.qr_fest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.usic.qr_fest.model.Entity.Persona;
import com.usic.qr_fest.model.Entity.Rol;
import com.usic.qr_fest.model.Entity.Usuario;
import com.usic.qr_fest.model.IService.IPersonaService;
import com.usic.qr_fest.model.IService.IRolService;
import com.usic.qr_fest.model.IService.IUsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IRolService rolService;

    @GetMapping(value = "/administrar_usuario")
    public String administrarUsuario(Model model) {
        
        return "usuario/vista-usuario";
    }

    @PostMapping("/FormularioUsuario")
    public String formularioUsuario(HttpServletRequest request, Model model) {
        model.addAttribute("usuarioss", new Usuario());
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("personas", personaService.findAll());
        return "usuario/formulario-usuario";
    }

    @GetMapping("/formularioEditUsuario/{idUsuario}")
    public String FormularioEditUsuario(HttpServletRequest request, Model model,
            @PathVariable("idUsuario") Long idUsuario) {
                Usuario usuario = usuarioService.findOne(idUsuario);
                List<Rol> roles = rolService.findAll();
                List<Persona> personas = personaService.findAll();
                model.addAttribute("usuarioss", usuario);
                model.addAttribute("roles", roles);
                model.addAttribute("personas", personas);
        model.addAttribute("edit", "true");
        return "usuario/formulario-usuario";
    }

    @PostMapping("/listarUsuario")
    public String ListarUsuario(HttpServletRequest request, Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/tabla-usuario";
    }

    @PostMapping("/registrarUsuario")
    public ResponseEntity<String> RegistrarUsuario(HttpServletRequest request, @Validated Usuario usuario) {
        if (usuarioService.findByUsername(usuario.getUsername())  == null) {
            usuario.setEstado("ACTIVO");
            usuarioService.save(usuario);
            return ResponseEntity.ok("Se guardó el registro con éxito");
        } else {
            return ResponseEntity.ok("Ya existe este registro");
        }
    }

    @PostMapping("/editarUsuario")
    public ResponseEntity<String> editarUsuario(@Validated Usuario usuario) {
        if (usuarioService.findByUsername(usuario.getUsername()) == null) {
            usuario.setEstado("ACTIVO");
            usuarioService.save(usuario);
            return ResponseEntity.ok("Se modificó el registro con éxito");
        }else{
            return ResponseEntity.ok("ya existe este usuario");
        }
    }

    @PostMapping("/eliminarUsuario/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(HttpServletRequest request,
            @PathVariable("idUsuario") Long idUsuario) {
        usuarioService.delete(idUsuario);
        return ResponseEntity.ok("Se eliminó el registro con éxito");
    }
}
