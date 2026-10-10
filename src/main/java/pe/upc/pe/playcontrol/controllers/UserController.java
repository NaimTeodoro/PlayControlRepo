package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite peticiones desde la app móvil o web
public class UserController {

    @Autowired
    private UserService userService;

    // GET para listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> listar() {
        List<User> usuarios = userService.listarUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // POST para registrar un nuevo usuario desde la pantalla de registro
    @PostMapping
    public ResponseEntity<User> registrar(@RequestBody User usuario) {
        User nuevoUsuario = userService.registrarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }
}