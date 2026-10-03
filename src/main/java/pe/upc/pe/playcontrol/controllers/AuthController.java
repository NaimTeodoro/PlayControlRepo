package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.AuthDTO;
import pe.upc.pe.playcontrol.dtos.TokenDTO;
import pe.upc.pe.playcontrol.dtos.UserRegisterDTO;
import pe.upc.pe.playcontrol.entities.Authority;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.security.JwtUtilService;
import pe.upc.pe.playcontrol.servicesimplements.UserDetailsServiceImpl;

import java.util.Collections;

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
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        // Contraseña encriptada con BCrypt
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setEnabled(true);

        Authority authority = new Authority();
        authority.setAuthority(dto.getRol() != null ? dto.getRol() : "ROLE_USER");
        user.setRoles(Collections.singletonList(authority));

        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthDTO authDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authDTO.getUsername());
        String token = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(new TokenDTO(token));
    }
}