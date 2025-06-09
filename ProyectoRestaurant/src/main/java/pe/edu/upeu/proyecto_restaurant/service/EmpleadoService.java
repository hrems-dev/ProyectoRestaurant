package pe.edu.upeu.proyecto_restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;
import pe.edu.upeu.proyecto_restaurant.repository.EmpleadoRepository;

import java.util.List;

@Service
public class EmpleadoService {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    public Empleado buscarPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public Empleado crear(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }

    public Empleado buscarPorUsuario(String usuario) {
        return empleadoRepository.findByUsuario(usuario);
    }

    // Login
    public Empleado loginEmpleado(String usuario, String password) {
        logger.info("Iniciando proceso de login para usuario: {}", usuario);
        logger.debug("Password length: {}", password != null ? password.length() : 0);
        
        // Primero buscamos el empleado por usuario para ver si existe
        Empleado empleadoExistente = empleadoRepository.buscarEmpleadoPorUsuario(usuario);
        if (empleadoExistente == null) {
            logger.warn("No se encontró ningún empleado con el usuario: {}", usuario);
            return null;
        }
        logger.debug("Empleado encontrado: {}", empleadoExistente);
        
        // Verificamos el rol
        String rol = empleadoExistente.getRol();
        logger.info("Rol del empleado: '{}' (sin trim)", rol);
        rol = rol != null ? rol.toLowerCase().trim() : null;
        logger.info("Rol del empleado (después de trim y toLowerCase): '{}'", rol);
        
        if (rol == null) {
            logger.warn("El empleado no tiene un rol asignado");
            return null;
        }
        
        // Verificar si el rol es válido (admin o caj)
        if (!rol.equals("admin") && !rol.equals("caj")) {
            logger.warn("Rol no válido para el usuario {}: '{}'", usuario, rol);
            return null;
        }
        
        // Intentar login
        Empleado empleado = empleadoRepository.loginEmpleado(usuario, password);
        
        if (empleado != null) {
            logger.info("Login exitoso para el usuario: {} con rol: {}", usuario, empleado.getRol());
            // Asegurarnos que el rol esté en el formato correcto
            empleado.setRol(rol);
        } else {
            logger.warn("Login fallido - contraseña incorrecta para el usuario: {}", usuario);
        }
        
        return empleado;
    }
} 