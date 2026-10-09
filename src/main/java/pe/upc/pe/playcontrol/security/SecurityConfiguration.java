package pe.upc.pe.playcontrol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Hashea las contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas públicas (Login, Registro, Swagger) que no piden token
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Rutas para el Padre / Tutor 
                        .requestMatchers("/api/padretutor/**", "/api/padrehijo/**", "/api/limitejuego/**")
                        .hasAnyAuthority("ROLE_PADRE", "ROLE_ADMIN")

                        // 3. Rutas para el Jugador Joven
                        .requestMatchers("/api/jugadorjoven/**", "/api/sesionjuego/**", "/api/recompensas/**", "/api/retos/**")
                        .hasAnyAuthority("ROLE_JUGADOR", "ROLE_ADMIN")

                        // 4. Rutas para el Profesional de Salud
                        .requestMatchers("/api/profesionalsalud/**", "/api/profesionalcliente/**", "/api/recursoeducativo/**")
                        .hasAnyAuthority("ROLE_PROFESIONAL", "ROLE_ADMIN")

                        // 5. El resto de rutas piden estar autenticado con CUALQUIER rol válido
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
