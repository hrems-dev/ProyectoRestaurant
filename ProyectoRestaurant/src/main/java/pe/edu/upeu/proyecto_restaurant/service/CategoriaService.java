package pe.edu.upeu.proyecto_restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.proyecto_restaurant.modulo.Categoria;
import pe.edu.upeu.proyecto_restaurant.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Crear o actualizar categoría
    public Categoria guardarCategoria(Categoria categoria) {
        logger.info("Guardando categoría: {}", categoria);
        return categoriaRepository.save(categoria);
    }

    // Listar todas las categorías
    public List<Categoria> listarTodos() {
        List<Categoria> categorias = categoriaRepository.findAll();
        logger.info("Total de categorías encontradas: {}", categorias.size());
        return categorias;
    }

    // Actualizar categoría
    public Categoria actualizar(Categoria categoria) {
        logger.info("Actualizando categoría: {}", categoria);
        if (categoriaRepository.existsById(categoria.getId())) {
            return categoriaRepository.save(categoria);
        }
        return null;
    }

    // Eliminar categoría
    public void eliminar(Long id) {
        logger.info("Eliminando categoría con ID: {}", id);
        categoriaRepository.deleteById(id);
    }

    // Buscar categoría por ID
    public Categoria buscarPorId(Long id) {
        logger.debug("Buscando categoría por ID: {}", id);
        return categoriaRepository.findById(id).orElse(null);
    }

    // Buscar categoría por nombre
    public Categoria buscarPorNombre(String nombre) {
        try {
            logger.debug("Buscando categoría por nombre: {}", nombre);
            return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    logger.debug("No se encontró categoría con nombre: {}", nombre);
                    return null;
                });
        } catch (Exception e) {
            logger.error("Error al buscar categoría por nombre: {}", nombre, e);
            return null;
        }
    }

    // Crear nueva categoría
    public Categoria crear(Categoria categoria) {
        logger.info("Creando nueva categoría: {}", categoria);
        return categoriaRepository.save(categoria);
    }
} 