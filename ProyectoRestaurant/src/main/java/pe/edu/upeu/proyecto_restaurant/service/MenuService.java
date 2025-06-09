package pe.edu.upeu.proyecto_restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.proyecto_restaurant.modulo.Menu;
import pe.edu.upeu.proyecto_restaurant.modulo.Pedido;
import pe.edu.upeu.proyecto_restaurant.modulo.Producto;
import pe.edu.upeu.proyecto_restaurant.repository.MenuRepository;
import pe.edu.upeu.proyecto_restaurant.repository.PedidoRepository;
import pe.edu.upeu.proyecto_restaurant.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Menu> listarTodos() {
        try {
            List<Menu> menuItems = menuRepository.findAll();
            logger.info("Total de items en el menú: {}", menuItems.size());
            return menuItems;
        } catch (Exception e) {
            logger.error("Error al listar items del menú", e);
            throw e;
        }
    }

    public Menu buscarPorId(Long id) {
        try {
            logger.debug("Buscando item del menú con ID: {}", id);
            return menuRepository.findById(id)
                .orElseGet(() -> {
                    logger.debug("No se encontró item del menú con ID: {}", id);
                    return null;
                });
        } catch (Exception e) {
            logger.error("Error al buscar item del menú por ID", e);
            throw e;
        }
    }

    public Menu guardar(Menu menu) {
        try {
            logger.info("Guardando item del menú: {}", menu);
            return menuRepository.save(menu);
        } catch (Exception e) {
            logger.error("Error al guardar item del menú", e);
            throw e;
        }
    }

    public Menu actualizar(Menu menu) {
        try {
            logger.info("Actualizando item del menú: {}", menu);
            if (menuRepository.existsById(menu.getIdMenu())) {
                return menuRepository.save(menu);
            }
            logger.warn("No se encontró el item del menú con ID: {}", menu.getIdMenu());
            return null;
        } catch (Exception e) {
            logger.error("Error al actualizar item del menú", e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        try {
            logger.info("Eliminando item del menú con ID: {}", id);
            menuRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar item del menú", e);
            throw e;
        }
    }

    public Menu agregarAlMenu(Long idPedido, Long idProducto, String codPedido) {
        try {
            logger.info("Agregando producto {} al pedido {}", idProducto, idPedido);
            
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
            Optional<Producto> productoOpt = productoRepository.findById(idProducto);
            
            if (!pedidoOpt.isPresent()) {
                logger.error("No se encontró el pedido con ID: {}", idPedido);
                return null;
            }
            
            if (!productoOpt.isPresent()) {
                logger.error("No se encontró el producto con ID: {}", idProducto);
                return null;
            }
            
            Menu menu = Menu.builder()
                    .pedido(pedidoOpt.get())
                    .producto(productoOpt.get())
                    .codPedido(codPedido)
                    .build();
            
            return menuRepository.save(menu);
        } catch (Exception e) {
            logger.error("Error al agregar producto al menú", e);
            throw e;
        }
    }

    // Obtener todos los items del menú
    public List<Menu> listarMenu() {
        List<Menu> menuItems = menuRepository.findAll();
        logger.info("Total de items en el menú: {}", menuItems.size());
        return menuItems;
    }

    // Eliminar item del menú
    public void eliminarDelMenu(Long idMenu) {
        logger.info("Eliminando item {} del menú", idMenu);
        menuRepository.deleteById(idMenu);
    }

    // Buscar item del menú por ID
    public Menu buscarItemMenu(Long idMenu) {
        logger.debug("Buscando item del menú con ID: {}", idMenu);
        return menuRepository.findById(idMenu).orElse(null);
    }
} 