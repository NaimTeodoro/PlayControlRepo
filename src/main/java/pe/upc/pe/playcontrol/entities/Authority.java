package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rol de un usuario (tabla rol_usuario). La FK id_usuario la gestiona User.roles.
 * authority: ROLE_ADMIN / ROLE_PADRE / ROLE_JUGADOR / ROLE_PROFESIONAL
 */
@Entity
@Table(name = "rol_usuario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority", nullable = false, length = 50)
    private String authority;
}
