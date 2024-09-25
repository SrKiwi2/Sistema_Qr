package com.usic.qr_fest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.qr_fest.model.Entity.Entrada;
import com.usic.qr_fest.model.Entity.Persona;
import com.usic.qr_fest.model.IService.IEntradaService;
import com.usic.qr_fest.model.IService.IPersonaService;

@Controller
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IEntradaService entradaService;

    @GetMapping(value = "/vista_persona")
    public String vistaPersona(Model model) {
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("persona", new Persona());
        return "/persona/adm_persona";
    }

    @GetMapping(value = "/listar_qr")
    public String listarEntradas(Model model) {
        List<Entrada> entradas = entradaService.findAll();
        model.addAttribute("entradas", entradas);
        return "/administracion/tabla_entradas";
    }

    @GetMapping("/persona/{idPersona}")
    @ResponseBody
    public Persona obtenerPersona(@PathVariable("idPersona") Long idPersona) {
        return personaService.findOne(idPersona);
    }

    @PostMapping(value = "/guardar-persona")
    public String guardarPersona(
        @RequestParam(value = "idPersona", required = false) Long idPersona,
        @RequestParam("nombre") String nombre,
        @RequestParam("paterno") String paterno,
        @RequestParam("materno") String materno,
        @RequestParam("ci") String ci,
        @RequestParam("rd") String codigo_funcionario,
        Model model) {

        Persona persona = personaService.findByCi(ci);
        if (persona == null) {
            persona = new Persona();
        }
        persona.setCi(ci);
        persona.setNombre(nombre);
        persona.setPaterno(paterno);
        persona.setMaterno(materno);
        persona.setCodigo_funcionario(codigo_funcionario);
        persona.setEstado("ACTIVO");
        personaService.save(persona);
        return "redirect:/vista_persona";
    }

    @PostMapping(value = "/eliminar-persona/{idPersona}")
    public ResponseEntity<String> eliminarPersona(@PathVariable("idPersona") Long idPersona) {
        personaService.delete(idPersona);
        return ResponseEntity.ok("Se eliminó el registro con éxito");
    }
}
