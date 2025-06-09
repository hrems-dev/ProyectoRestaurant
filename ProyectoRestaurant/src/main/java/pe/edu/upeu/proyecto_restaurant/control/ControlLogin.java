package pe.edu.upeu.proyecto_restaurant.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;
import pe.edu.upeu.proyecto_restaurant.service.EmpleadoService;
import pe.edu.upeu.proyecto_restaurant.utils.StageManager;

@Controller
public class ControlLogin {
    private static final Logger logger = LoggerFactory.getLogger(ControlLogin.class);
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @FXML
    private TextField txtUsuario;
    
    @FXML
    private PasswordField txtClave;
    
    @FXML
    private Button btnIngresar;

    @FXML
    public void login(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText();
            String clave = txtClave.getText();
            
            // Validación de campos vacíos
            if (usuario.isEmpty() || clave.isEmpty()) {
                logger.warn("Intento de login con campos vacíos");
                showErrorMessage("Usuario y contraseña son requeridos");
                return;
            }
            
            logger.debug("Intentando autenticar usuario: {}", usuario);
            
            // Buscar empleado por usuario
            Empleado empleado = empleadoService.buscarPorUsuario(usuario);
            
            if (empleado != null && empleado.getPassword().equals(clave) && "activo".equalsIgnoreCase(empleado.getEstado())) {
                logger.info("Autenticación exitosa para el empleado: {} con rol: {}", empleado.getNombre(), empleado.getRol());
                
                // Store employee session data
                StageManager.setCurrentEmpleado(empleado);
                
                try {
                    // Cargar el mainfrm.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainfrm.fxml"));
                    loader.setControllerFactory(context::getBean);
                    Parent mainRoot = loader.load();
                    
                    // Obtener el controlador del mainfrm
                    GUIMainFX mainController = loader.getController();
                    
                    // Configurar la ventana principal
                    Stage stage = new Stage();
                    Scene scene = new Scene(mainRoot);
                    
                    // Configurar dimensiones de pantalla
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    stage.setX(bounds.getMinX());
                    stage.setY(bounds.getMinY());
                    stage.setWidth(bounds.getWidth());
                    stage.setHeight(bounds.getHeight());
                    
                    // Configurar la escena
                    stage.setScene(scene);
                    stage.setTitle("Sistema Restaurante - " + empleado.getRol().toUpperCase());
                    stage.setMaximized(true);
                    
                    // Cerrar ventana de login
                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                    
                    // Inicializar el controlador principal según el rol
                    String rol = empleado.getRol().toLowerCase().trim();
                    switch (rol) {
                        case "admin":
                            mainController.cargarVista("/views/admMenu.fxml", "Menú Administración");
                            break;
                        case "cajero":
                            mainController.cargarVista("/views/frmPedido.fxml", "Gestión de Pedidos");
                            break;
                        default:
                            logger.error("Rol no reconocido: '{}'", rol);
                            showErrorMessage("Rol no autorizado para acceder al sistema: " + rol);
                            return;
                    }
                    
                    // Guardar referencia del stage y mostrar
                    StageManager.setPrimaryStage(stage);
                    stage.show();
                    
                    logger.info("Interfaz principal cargada correctamente para el rol: {}", rol);
                } catch (Exception e) {
                    logger.error("Error al cargar la interfaz:", e);
                    showErrorMessage("Error al cargar la interfaz: " + e.getMessage());
                }
            } else {
                logger.warn("Error de autenticación: Credenciales inválidas para usuario: {}", usuario);
                showErrorMessage("Credenciales inválidas o usuario inactivo. Por favor, intente nuevamente.");
            }
        } catch (Exception e) {
            logger.error("Error inesperado durante la autenticación:", e);
            showErrorMessage("Error al iniciar sesión: " + e.getMessage());
        }
    }
    
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
