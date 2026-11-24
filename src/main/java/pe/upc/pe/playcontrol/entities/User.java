package pe.upc.pe.playcontrol.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Usuario del sistema (tabla usuario).
 * estado: ACTIVO / INACTIVO
 * nivelTecnologia: BASICO / MEDIO / ALTO
 */
@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    // Campos de seguridad
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    // Datos personales
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

    // Roles (tabla rol_usuario, FK id_usuario)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_usuario", nullable = false)
    private List<Authority> roles;
}
