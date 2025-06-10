package pe.edu.upeu.proyecto_restaurant.modulo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seccion")
public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seccion_id")
    private Long id;
    
    @Column(name = "nombre", nullable = false, columnDefinition = "varchar(255) default 'Sin nombre'")
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "estado")
    private String estado = "Activo";
} 