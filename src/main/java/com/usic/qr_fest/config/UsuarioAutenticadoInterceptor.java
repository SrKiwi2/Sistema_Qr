package com.usic.qr_fest.config;

import com.usic.qr_fest.anotaciones.ValidarUsuarioAutenticado;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
public class UsuarioAutenticadoInterceptor implements HandlerInterceptor {

  /**
   * Los interceptores son una estrategia que permite realizar funcionalidades
   * antes, después y finalizada una petición,
   * es importante agregar el interceptor en el registrador de interceptores en la
   * clase
   *
   * @see MvcConfig , el cual se sobreescribe su metodo addInterceptors
   *      este permite agregar más interceptores si se requiere, en este caso,
   *      registramos un interceptor, para que se ejecute en toda la aplicacion
   *      web
   *      <p>
   *      Sobreescritura del metodo postHandle, este metodo se ejecuta después de
   *      manejar una peticion, y antes de finalizar la peticion
   */
  /*
   * @Override
   * public void postHandle(@NonNull HttpServletRequest request, @NonNull
   * HttpServletResponse response,
   * 
   * @NonNull Object handler, ModelAndView modelAndView) {
   * 
   * if (handler instanceof HandlerMethod handlerMethod) {
   * //En cada petición se verifica si el método que realiza una petición esta
   * anotado con @ValidarUsuarioAutenticado
   * ValidarUsuarioAutenticado anotación =
   * handlerMethod.getMethodAnnotation(ValidarUsuarioAutenticado.class);
   * if (anotación != null) {
   * //En caso de que si esté anotado, se obtiene la sesión de la anotación
   * HttpSession session = request.getSession();
   * 
   * //Si en la sesión no está persona, significa que no está autenticado,
   * entonces redirigir al login de autenticación
   * if (session.getAttribute("persona") == null) {
   * modelAndView.setView(new RedirectView("/LoginAdm"));
   * }
   * }
   * }
   * }
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (handler instanceof HandlerMethod handlerMethod) {
      ValidarUsuarioAutenticado anotacion = handlerMethod.getMethodAnnotation(ValidarUsuarioAutenticado.class);
      if (anotacion != null) {
        HttpSession session = request.getSession();

        if (session.getAttribute("persona") == null) {
          response.sendRedirect("/form-login");
          return false;
        }
      }
    }
    return true;
  }
}