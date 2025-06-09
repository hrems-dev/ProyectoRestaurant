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
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "dni", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(name = "usuario", length = 50, nullable = false, unique = true)
    private String usuario;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "rol", length = 20, nullable = false)
    private String rol;

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = "activo";
        }
    }
} 