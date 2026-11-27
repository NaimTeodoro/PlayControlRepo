package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pe.playcontrol.dtos.AuthDTO;
import pe.upc.pe.playcontrol.dtos.TokenDTO;
import pe.upc.pe.playcontrol.dtos.UserRegisterDTO;
import pe.upc.pe.playcontrol.dtos.UsuarioActualDTO;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.security.JwtUtilService;
import pe.upc.pe.playcontrol.services.UserService;
import pe.upc.pe.playcontrol.servicesimplements.UserDetailsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Registro publico. Crea el usuario y, en la misma transaccion, su perfil segun el rol
     * (jugador con codigo de vinculacion, padre/tutor o profesional PENDIENTE).
     * Respuesta: {mensaje, idUsuario, rol}
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody UserRegisterDTO dto) {
        return ResponseEntity.ok(userService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthDTO authDTO) {
        // Si las credenciales son incorrectas lanza BadCredentialsException -> 401 (GlobalExceptionHandler)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authDTO.getUsername());
        User user = userRepository.findByUsername(authDTO.getUsername()).orElseThrow();
        user.setUltimoAcceso(LocalDateTime.now());
        userRepository.save(user);
        String token = jwtUtilService.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        return ResponseEntity.ok(new TokenDTO(token, user.getIdUsuario(), user.getUsername(), roles));
    }

    /**
     * Datos del usuario autenticado: {idUsuario, username, nombre, apellido, email, roles,
     * idJugadorJoven, idPadreTutor, idProfesional} (los id de perfil son null si no aplican).
     */
    @GetMapping("/me")
    public UsuarioActualDTO me(Authentication authentication) {
        return userService.obtenerActual(authentication.getName());
    }
}
