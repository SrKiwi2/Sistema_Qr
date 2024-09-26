package com.usic.qr_fest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.usic.qr_fest.model.Entity.Persona;
import com.usic.qr_fest.model.Entity.Rol;
import com.usic.qr_fest.model.Entity.Usuario;
import com.usic.qr_fest.model.IService.IPersonaService;
import com.usic.qr_fest.model.IService.IRolService;
import com.usic.qr_fest.model.IService.IUsuarioService;

@SpringBootApplication
public class QrFestApplication {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IRolService rolService;

	public static void main(String[] args) {
		SpringApplication.run(QrFestApplication.class, args);
	}

	@Bean
	ApplicationRunner init(IUsuarioService usuarioService, IPersonaService personaService, IRolService rolService){
		return args -> {
			System.out.println("SISTEMA ORIENTACION VOCACIONAL INICIANDO...");

			String[] roles = {"SUPER USUARIO", "ADMINISTRADOR", "ESCANEADOR"};
			Rol[] rolObjects = new Rol[roles.length];
			for (int i = 0; i < roles.length; i++) {
                Rol rol = rolService.findByNombreRol(roles[i]);
                if (rol == null) {
                    rol = new Rol();
                    rol.setNombreRol(roles[i]);
					rol.setEstado("ACTIVO");
                    rolService.save(rol);
                }
                rolObjects[i] = rol;
            }

			String[] cis = { "111", "222" };
            String[] nombres = { "PRIMER USUARIO", "SEGUNDO USUARIO" };
            String[] usuarios = { "admin1", "admin2" };
			String[] password = { "123", "456" };
            for (int i = 0; i < cis.length; i++) {
                // Verificar si la persona ya existe
                Persona persona = personaService.findByCi(cis[i]);
                if (persona == null) {
                    persona = new Persona();
                    persona.setNombre(nombres[i]);
                    persona.setPaterno("ApellidoP" + (i + 1));
                    persona.setMaterno("ApellidoM" + (i + 1));
                    persona.setCi(cis[i]);
					persona.setEstado("ACTIVO");
                    personaService.save(persona);
                }

                // Verificar si el usuario ya existe
                Usuario usuario = usuarioService.findByUsername(usuarios[i]);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setUsername(usuarios[i]);
                    usuario.setPassword(password[i]);
                    usuario.setPersona(persona); // Asociar la persona con el usuario
                    usuario.setRol(rolObjects[i % roles.length]); // Asignar el rol correspondiente
					usuario.setEstado("ACTIVO");
                    usuarioService.save(usuario);
                }
            }
		};
	}
}
