package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    // Campos de Seguridad (Turno 2)
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    // Nuevos campos del Turno 1
    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "apellido", length = 100)
    private String apellido;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "telefono", length = 20)
    private String telefono;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column(name = "genero", length = 20)
    private String genero;
    @Column(name = "estado", length = 20)
    private String estado;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    @Column(name = "pais", length = 100)
    private String pais;
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    @Column(name = "direccion", length = 255)
    private String direccion;
    @Column(name = "nivel_tecnologia", length = 20)
    private String nivelTecnologia;
    @Column(name = "configuracion_privacidad", length = 500)
    private String configuracionPrivacidad;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> roles;
}