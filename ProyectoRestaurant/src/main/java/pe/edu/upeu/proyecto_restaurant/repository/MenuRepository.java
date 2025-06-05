package pe.edu.upeu.proyecto_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.proyecto_restaurant.modulo.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
} 