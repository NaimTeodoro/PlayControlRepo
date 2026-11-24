package pe.upc.pe.playcontrol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Orígenes permitidos para el front (Angular). Separar con comas si hay varios.
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).toList());
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        cfg.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Preflight de CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1. Públicas: login, registro y Swagger. /me exige token.
                        //    /error se deja público para que los 404/405 de Spring no se conviertan en 401/403
                        //    al reenviarse a la página de error (p. ej. GET /api/juegos, ruta eliminada -> 404).
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        // 2. Solo administrador
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")
                        // Evidencia de juego: borrar sesiones, alertas e historial de alertas solo ADMIN
                        // (va antes de las reglas generales de esas rutas)
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/sesiones/**", "/api/alertas-juego/**", "/api/historial-alertas/**")
                        .hasAuthority("ROLE_ADMIN")

                        // 3. Jugador joven: lo leen jugador, padre, profesional y admin; lo modifican jugador y admin
                        .requestMatchers(HttpMethod.GET, "/api/jugadores-jovenes/**")
                        .hasAnyAuthority("ROLE_JUGADOR", "ROLE_PADRE", "ROLE_PROFESIONAL", "ROLE_ADMIN")
                        .requestMatchers("/api/jugadores-jovenes/**").hasAnyAuthority("ROLE_JUGADOR", "ROLE_ADMIN")

                        // 4. Padre/tutor
                        .requestMatchers(HttpMethod.GET, "/api/padres-tutores/**").hasAnyAuthority("ROLE_PADRE", "ROLE_ADMIN")
                        .requestMatchers("/api/padres-tutores/**").hasAnyAuthority("ROLE_PADRE", "ROLE_ADMIN")

                        // 5. Compartidas jugador + padre (el profesional también puede LEER sesiones y alertas)
                        .requestMatchers(HttpMethod.GET, "/api/sesiones/**", "/api/alertas-juego/**")
                        .hasAnyAuthority("ROLE_JUGADOR", "ROLE_PADRE", "ROLE_PROFESIONAL", "ROLE_ADMIN")
                        .requestMatchers(
                                "/api/limites-juego/**", "/api/alertas-juego/**", "/api/historial-alertas/**",
                                "/api/padre-hijo/**", "/api/sesiones/**", "/api/recompensas/**",
                                "/api/dispositivos/**", "/api/usuario-recursos/**")
                        .hasAnyAuthority("ROLE_JUGADOR", "ROLE_PADRE", "ROLE_ADMIN")

                        // 6. Reportes para padres
                        .requestMatchers("/api/reportes-padre/**").hasAnyAuthority("ROLE_PADRE", "ROLE_ADMIN")

                        // 7. Profesional de salud
                        .requestMatchers("/api/profesionales-salud/**", "/api/profesional-clientes/**")
                        .hasAnyAuthority("ROLE_PROFESIONAL", "ROLE_ADMIN")

                        // 8. Catálogos: leer cualquier autenticado; escribir recursos -> profesional/admin, el resto -> admin
                        .requestMatchers(HttpMethod.GET, "/api/recursos-educativos/**", "/api/logros/**",
                                "/api/videojuegos/**", "/api/plataformas/**").authenticated()
                        .requestMatchers("/api/recursos-educativos/**").hasAnyAuthority("ROLE_PROFESIONAL", "ROLE_ADMIN")
                        .requestMatchers("/api/logros/**", "/api/videojuegos/**", "/api/plataformas/**").hasAuthority("ROLE_ADMIN")

                        // Citas con profesionales: solo lectura para jugador, padre y admin (DTO sin datos clínicos)
                        .requestMatchers(HttpMethod.GET, "/api/citas/**").hasAnyAuthority("ROLE_JUGADOR", "ROLE_PADRE", "ROLE_ADMIN")
                        .requestMatchers("/api/citas/**").denyAll()

                        // 9. Todo lo demás exige token
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
