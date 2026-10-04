package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "retos_usuarios")
public class RetoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRetoUsuario;

    @Column(nullable = false, length = 20)
    private String estado; // Ejemplo: "PENDIENTE", "COMPLETADO"

    @Column(nullable = true)
    private LocalDate fechaCompletado;

    // Relación con la entidad User existente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reto", nullable = false)
    private Reto reto;
}
