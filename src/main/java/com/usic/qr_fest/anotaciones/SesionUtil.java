package com.usic.qr_fest.anotaciones;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Component
public class SesionUtil {

    public String verificarSesionYRol(HttpSession session, RedirectAttributes redirectAttributes, String... rolesPermitidos) {
        
        if (session.getAttribute("usuario") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder a esta página");
            return "redirect:/form-login";
        }

        
        String rolUsuario = (String) session.getAttribute("nombreRol");
        boolean tieneRolPermitido = false;
        for (String rol : rolesPermitidos) {
            if (rol.equals(rolUsuario)) {
                tieneRolPermitido = true;
                break;
            }
        }

        if (!tieneRolPermitido) {
            redirectAttributes.addFlashAttribute("error", "No tiene permiso para acceder a esta página");
            return "redirect:/form-login"; 
        }

        return null;
    }

}
