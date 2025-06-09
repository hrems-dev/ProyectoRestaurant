package pe.edu.upeu.proyecto_restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.proyecto_restaurant.modulo.Seccion;
import pe.edu.upeu.proyecto_restaurant.repository.SeccionRepository;

import java.util.List;

@Service
public class SeccionService {
    private static final Logger logger = LoggerFactory.getLogger(SeccionService.class);

    @Autowired
    private SeccionRepository seccionRepository;

    public List<Seccion> listarTodos() {
        return seccionRepository.findAll();
    }

    public Seccion guardar(Seccion seccion) {
        return seccionRepository.save(seccion);
    }

    public Seccion actualizar(Seccion seccion) {
        try {
            logger.info("Actualizando sección: {}", seccion);
            if (seccionRepository.existsById(seccion.getId())) {
                return seccionRepository.save(seccion);
            }
            logger.warn("No se encontró la sección con ID: {}", seccion.getId());
            return null;
        } catch (Exception e) {
            logger.error("Error al actualizar sección", e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        seccionRepository.deleteById(id);
    }

    public Seccion buscarPorId(Long id) {
        return seccionRepository.findById(id).orElse(null);
    }

    public Seccion buscarPorNombre(String nombre) {
        try {
            logger.debug("Buscando sección por nombre: {}", nombre);
            return seccionRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    logger.debug("No se encontró sección con nombre: {}", nombre);
                    return null;
                });
        } catch (Exception e) {
            logger.error("Error al buscar sección por nombre", e);
            throw e;
        }
    }

    public Seccion crearNuevaSeccion(String nombre) {
        try {
            logger.info("Creando nueva sección con nombre: {}", nombre);
            Seccion seccion = Seccion.builder()
                    .nombre(nombre)
                    .estado("Activo")
                    .build();
            return seccionRepository.save(seccion);
        } catch (Exception e) {
            logger.error("Error al crear nueva sección", e);
            throw e;
        }
    }
} 