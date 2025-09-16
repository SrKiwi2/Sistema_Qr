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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    private IUsuarioService  usuarioService;

    private static final String ROL_ESCANEADOR = "ESCANEADOR";
    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";

    @GetMapping("/")
    public String getMethodName() {
        return "redirect:/form-login";
    }
    

    @GetMapping(value = "/form-login")
    public String formLogin() {

        return "login/login";
    }

    // @PostMapping("/iniciar-sesion")
    // public String iniciarSesion(@RequestParam(value = "usuario") String username,
    //                         @RequestParam(value = "contrasena") String contrasena, 
    //                         Model model, HttpServletRequest request,
    //                         RedirectAttributes flash) {

    //     Usuario usuario = usuarioService.getUsuarioPassword(username, contrasena);

    //     if (usuario != null) {

    //         if (usuario.getEstado().equals("INHABILITADO")) {
    //             System.out.println("NO ESTA ACTIVO ESTE USUARIO");
    //             return "redirect:/form-login";
    //         }

    //         if(usuario.getRol().equals("ESCANEADOR")){
    //             System.out.println("escaneador 1 activo");
    //             return "redirect:/escaneo";
    //         }

    //         HttpSession sessionAdministrador = request.getSession(true);
    //         sessionAdministrador.setAttribute("usuario", usuario);
    //         sessionAdministrador.setAttribute("persona", usuario.getPersona());
    //         sessionAdministrador.setAttribute("nombre_rol", usuario.getRol().getNombreRol());
    //         flash.addAttribute("success", usuario.getPersona().getNombre());
    //         System.out.println("El usuario " + usuario.getPersona().getNombre() + " ha iniciado sesión como administrador.");
    //         return "redirect:/vista_administrador";

    //     } else {
    //         return "redirect:/form-login";
    //     }
    // }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam("usuario") String username,
                                @RequestParam("contrasena") String contrasena,
                                HttpServletRequest request,
                                RedirectAttributes flash) {

        // 1) Normaliza entradas
        username   = username == null ? "" : username.trim();
        contrasena = contrasena == null ? "" : contrasena;

        // 2) Autentica
        Usuario usuario = usuarioService.getUsuarioPassword(username, contrasena);

        if (usuario == null) {
            flash.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
            // logAccesoService.loginFallido(username, request.getRemoteAddr());
            return "redirect:/form-login";
        }

        // 3) Estado de cuenta
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            flash.addFlashAttribute("warning", "Tu cuenta se encuentra inhabilitada. Contacta al administrador.");
            return "redirect:/form-login";
        }

        // 4) Refresca/rota sesión para evitar fixation
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();
        HttpSession session = request.getSession(true);
        request.changeSessionId();

        // 5) Coloca en sesión toda la info necesaria para cualquier rol
        session.setAttribute("usuario", usuario);
        session.setAttribute("persona", usuario.getPersona());
        String nombreRol = (usuario.getRol() != null ? usuario.getRol().getNombreRol() : null);
        session.setAttribute("nombre_rol", nombreRol);
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("username", username);

        flash.addFlashAttribute("success", "¡Bienvenido, " +
                (usuario.getPersona() != null ? usuario.getPersona().getNombre() : username) + "!");

        // 6) Redirección por rol
        if (ROL_ESCANEADOR.equalsIgnoreCase(nombreRol)) {
            log.info("Usuario {} ha iniciado sesión como ESCANEADOR.", username);
            return "redirect:/escaneo";
        }

        // Por defecto, admin o cualquier otro rol
        if (ROL_ADMINISTRADOR.equalsIgnoreCase(nombreRol)) {
            log.info("Usuario {} ha iniciado sesión como ADMINISTRADOR.", username);
            return "redirect:/vista_administrador";
        }

        log.info("Usuario {} ha iniciado sesión con rol {}.", username, nombreRol);
        return "redirect:/inicio";
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
        return "redirect:/";
    }
}
