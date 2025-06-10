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
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long idProducto;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id")
    private Categoria categoria;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "estado", length = 20)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "seccion_id", referencedColumnName = "seccion_id")
    private Seccion seccion;

    @Column(name = "imagen", length = 500)
    private String imagen;
}
