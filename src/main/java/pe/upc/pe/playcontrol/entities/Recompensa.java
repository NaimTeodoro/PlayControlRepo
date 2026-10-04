package pe.upc.pe.playcontrol.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recompensas")
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecompensa;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Integer costoPuntos;
}