package pe.edu.upeu.proyecto_restaurant.modulo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "mesa")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Long idMesa;

    @Column(name = "numero", nullable = false, unique = true)
    private Integer numero;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "detalle", length = 200)
    private String detalle;

    @Column(name = "estado", length = 20)
    private String estado; // Ocupado, desocupado, no habilitado, limpieza
}
