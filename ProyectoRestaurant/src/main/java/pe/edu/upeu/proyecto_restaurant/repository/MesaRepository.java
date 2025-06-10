package pe.edu.upeu.proyecto_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.proyecto_restaurant.modulo.Mesa;
 
@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
} 