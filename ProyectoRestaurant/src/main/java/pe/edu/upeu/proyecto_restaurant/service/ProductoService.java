package pe.edu.upeu.proyecto_restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.proyecto_restaurant.modulo.Producto;
import pe.edu.upeu.proyecto_restaurant.repository.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    // Crear o actualizar producto
    public Producto guardarProducto(Producto producto) {
        logger.info("Guardando producto: {}", producto);
        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        logger.info("Total de productos encontrados: {}", productos.size());
        return productos;
    }

    // Actualizar producto
    public Producto actualizarProducto(Producto producto) {
        logger.info("Actualizando producto: {}", producto);
        return productoRepository.save(producto);
    }

    // Eliminar producto
    public void eliminarProducto(Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

    // Buscar producto por ID
    public Producto buscarProducto(Long id) {
        logger.debug("Buscando producto por ID: {}", id);
        return productoRepository.findById(id).orElse(null);
    }

    // Actualizar stock de producto
    public boolean actualizarStock(Long idProducto, Integer cantidad) {
        logger.info("Actualizando stock del producto {} en {}", idProducto, cantidad);
        
        Producto producto = buscarProducto(idProducto);
        if (producto != null) {
            Integer nuevoStock = producto.getStock() + cantidad;
            if (nuevoStock >= 0) {
                producto.setStock(nuevoStock);
                productoRepository.save(producto);
                return true;
            }
        }
        return false;
    }
} 