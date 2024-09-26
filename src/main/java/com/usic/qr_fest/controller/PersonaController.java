package com.usic.qr_fest.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

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
    @ResponseBody
    public ResponseEntity<String> guardarPersona(
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
        return ResponseEntity.ok("Persona guardada correctamente");
    }

    @PostMapping(value = "/eliminar-persona/{idPersona}")
    public ResponseEntity<String> eliminarPersona(@PathVariable("idPersona") Long idPersona) {
        personaService.delete(idPersona);
        return ResponseEntity.ok("Se eliminó el registro con éxito");
    }
    
    @PostMapping("/registrarPersona")
    public ResponseEntity<String> registrarPersona(@RequestParam(name = "codigo_funcionario") String codFuncionario) {
        try {
            // Llamar a la API para obtener los datos del funcionario
            String url = "http://172.16.21.2:3333/api/londraPost/v1/obtenerDatos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(
                    "{\"usuario\":\"" + codFuncionario + "\", \"contrasena\":\"\"}", headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getStatusCodeValue() == 200) {
                Map<String, Object> data = responseEntity.getBody();
                if (data != null && data.get("per_num_doc") != null) {
                    // Obtener los datos del funcionario desde la API
                    String ci = data.get("per_num_doc").toString();
                    String nombre = data.get("per_nombres").toString();
                    String paterno = data.get("per_ap_paterno").toString();
                    String materno = data.get("per_ap_materno").toString();

                    // Buscar si la persona ya existe en la base de datos
                    Persona personaExistente = personaService.findByCi(ci);
                    if (personaExistente == null) {
                        personaExistente = new Persona();
                    }

                    // Actualizar o establecer los datos de la persona
                    personaExistente.setNombre(nombre.toUpperCase());
                    personaExistente.setPaterno(paterno.toUpperCase());
                    personaExistente.setMaterno(materno.toUpperCase());
                    personaExistente.setCodigo_funcionario(codFuncionario);
                    personaExistente.setCi(ci);
                    personaExistente.setEstado("ACTIVO");

                    // Guardar o actualizar la persona en la base de datos
                    personaService.save(personaExistente);

                    return ResponseEntity.ok("Persona guardada o actualizada correctamente");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No se encontraron datos para el funcionario proporcionado");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se pudo obtener los datos del funcionario");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }

}
