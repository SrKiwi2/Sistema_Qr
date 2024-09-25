package com.usic.qr_fest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.qr_fest.model.Entity.Entrada;
import com.usic.qr_fest.model.IService.IEntradaService;
import com.usic.qr_fest.model.Service.QRService;


@Controller
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired
    private QRService qrService;

    @Autowired
    private IEntradaService entradaService;

    @GetMapping("/generarQr")
    public String vistaGenerarQr(Model model) {
        return "/vista_adm_qr/generar_qr";
    }

    @GetMapping("/validacion")
    public String vistaValidadQr() {
        return "/vista_adm_qr/validar_qr";
    }

    @PostMapping("/generar")
    @ResponseBody
    public Map<String, String> generarQrEntradas(@RequestParam int cantidad, @RequestParam String titulo) {
        Map<String, String> response = new HashMap<>();
        try {
            qrService.generarQrs(cantidad, titulo);
            response.put("mensaje", "QR generados correctamente.");
        } catch (Exception e) {
            response.put("error", "Error generando QR");
        }
        return response;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> descargarQrs() throws IOException {
        File folder = new File("src/main/resources/static/");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("qrs_") && name.endsWith(".zip"));
        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No se encontró el archivo ZIP.");
        }

        File zipFile = files[files.length - 1];

        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(zipFile.length())
            .body(resource);
    }

    @PostMapping("/validar")
    public ResponseEntity<Map<String, String>> validarQr(@RequestBody Map<String, String> body) {

        String identificadorEncriptado = body.get("identificador");
        Entrada entrada = entradaService.findByIdentificador(identificadorEncriptado);
        Map<String, String> response = new HashMap<>();

        if (entrada != null) {
            if ("APROBADO".equals(entrada.getEstado())) {
                response.put("mensaje", "aprobado");
            } else {
                response.put("mensaje", "no_aprobado");
            }
        } else {
            response.put("mensaje", "no_existe");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aprobar")
    public ResponseEntity<Map<String, String>> aprobarQr(@RequestBody Map<String, String> body) {

        String identificadorEncriptado = body.get("identificador");
        Entrada entrada = entradaService.findByIdentificador(identificadorEncriptado);
        Map<String, String> response = new HashMap<>();

        if (entrada != null && "NO APROBADO".equals(entrada.getEstado())) {
            entrada.setEstado("APROBADO");
            entradaService.save(entrada);
            response.put("mensaje", "QR aprobado exitosamente.");
        } else if (entrada != null && "APROBADO".equals(entrada.getEstado())) {
            response.put("mensaje", "El QR ya ha sido aprobado, está inválido.");
        } else {
            response.put("mensaje", "El QR no existe.");
        }

        return ResponseEntity.ok(response);
    }
}
