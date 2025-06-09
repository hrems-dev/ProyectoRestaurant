package pe.edu.upeu.proyecto_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    @Query(value = "SELECT e.* FROM empleado e WHERE e.usuario = :usuariox", nativeQuery = true)
    Empleado buscarEmpleadoPorUsuario(@Param("usuariox") String usuariox);

    @Query(value = "SELECT e.* FROM empleado e WHERE e.usuario = :usuario AND e.password = :password", nativeQuery = true)
    Empleado loginEmpleado(@Param("usuario") String usuario, @Param("password") String password);

    Empleado findByUsuario(String usuario);
} 