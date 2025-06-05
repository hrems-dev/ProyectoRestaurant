package pe.edu.upeu.proyecto_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.proyecto_restaurant.modulo.Seccion;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {
} 