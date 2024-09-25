package com.usic.qr_fest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.qr_fest.model.Entity.Usuario;
import com.usic.qr_fest.model.IService.IUsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private IUsuarioService  usuarioService;

    @GetMapping(value = "/form-login")
    public String formLogin() {

        return "login/login";
    }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam(value = "usuario") String username,
                            @RequestParam(value = "contrasena") String contrasena, 
                            Model model, HttpServletRequest request,
                            RedirectAttributes flash) {

        Usuario usuario = usuarioService.getUsuarioPassword(username, contrasena);

        if (usuario != null) {

            if (usuario.getEstado().equals("INHABILITADO")) {
                System.out.println("NO ESTA ACTIVO ESTE USUARIO");
                return "redirect:/form-login";
            }

            HttpSession sessionAdministrador = request.getSession(true);
            sessionAdministrador.setAttribute("usuario", usuario);
            sessionAdministrador.setAttribute("persona", usuario.getPersona());
            sessionAdministrador.setAttribute("nombre_rol", usuario.getRol().getNombreRol());
            flash.addAttribute("success", usuario.getPersona().getNombre());
            System.out.println("El usuario " + usuario.getPersona().getNombre() + " ha iniciado sesión como administrador.");
            return "redirect:/vista_administrador";

        } else {
            return "redirect:/form-login";
        }
    }

    @RequestMapping("/cerrar_sesion")
    public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash) {
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
            
            if (usuarioLogueado != null) {
                System.out.println("LA PERSONA "+usuarioLogueado.getPersona().getNombre()+" "+usuarioLogueado.getPersona().getPaterno()+" "+usuarioLogueado.getPersona().getMaterno()+ " HA CERRADO SESIÓN");
            }

            flash.addAttribute("validado", "Se cerro sesion con exito");
        }
        return "redirect:/vista-test";
    }
}
