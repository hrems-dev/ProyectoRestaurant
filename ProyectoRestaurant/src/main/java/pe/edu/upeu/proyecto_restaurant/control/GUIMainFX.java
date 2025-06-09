package pe.edu.upeu.proyecto_restaurant.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;
import pe.edu.upeu.proyecto_restaurant.utils.StageManager;

@Controller
public class GUIMainFX {
    private static final Logger logger = LoggerFactory.getLogger(GUIMainFX.class);

    @FXML
    private BorderPane bp;
    
    @FXML
    private MenuBar menuBarFx;
    
    @FXML
    private TabPane tabPaneFx;
    
    @Autowired
    private ApplicationContext context;
    
    @FXML
    public void initialize() {
        logger.info("Inicializando GUIMainFX");
        
        // Configurar visibilidad de menús según el rol
        configurarMenusPorRol();
    }
    
    private void configurarMenusPorRol() {
        Empleado empleado = StageManager.getCurrentEmpleado();
        if (empleado != null) {
            String rol = empleado.getRol().toLowerCase().trim();
            
            // Obtener el menú "Ventanas"
            Menu menuVentanas = menuBarFx.getMenus().stream()
                    .filter(menu -> menu.getText().equals("Ventanas"))
                    .findFirst()
                    .orElse(null);
            
            if (menuVentanas != null) {
                // Configurar visibilidad de items según el rol
                menuVentanas.getItems().forEach(item -> {
                    if (item instanceof MenuItem) {
                        MenuItem menuItem = (MenuItem) item;
                        String itemText = menuItem.getText();
                        
                        switch (rol) {
                            case "admin":
                                // El administrador ve todos los menús
                                menuItem.setVisible(true);
                                break;
                            case "cajero":
                                // El cajero solo ve "Pedidos"
                                menuItem.setVisible(itemText.equals("Pedidos"));
                                break;
                            default:
                                menuItem.setVisible(false);
                                break;
                        }
                        
                        // Configuración específica para "Empleados"
                        if (itemText.equals("Empleados")) {
                            menuItem.setVisible(rol.equals("admin"));
                        }
                    }
                });
            }
        }
    }
    
    public void cargarVista(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            
            Tab tab = new Tab(titulo);
            tab.setContent(loader.load());
            tab.setClosable(false);
            
            tabPaneFx.getTabs().add(tab);
            tabPaneFx.getSelectionModel().select(tab);
            
            logger.info("Vista cargada correctamente: {}", fxmlPath);
        } catch (Exception e) {
            logger.error("Error al cargar la vista {}: {}", fxmlPath, e.getMessage());
            mostrarError("Error al cargar la vista", e.getMessage());
        }
    }
    
    @FXML
    private void cerrarSesion() {
        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cierre de sesión");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro que desea cerrar la sesión?");
            
            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                StageManager.clearSession();
                Stage stage = (Stage) bp.getScene().getWindow();
                stage.close();
                StageManager.mostrarLogin();
            }
        } catch (Exception e) {
            logger.error("Error al cerrar sesión: {}", e.getMessage());
            mostrarError("Error", "No se pudo cerrar la sesión: " + e.getMessage());
        }
    }
    
    @FXML
    private void salir() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar salida");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro que desea salir de la aplicación?");
        
        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
    @FXML
    private void abrirMenu() {
        cargarVista("/views/admMenu.fxml", "Menú Administración");
    }
    
    @FXML
    private void abrirPedidos() {
        cargarVista("/views/frmPedido.fxml", "Gestión de Pedidos");
    }

    @FXML
    private void abrirEmpleados() {
        cargarVista("/views/frmEmpleado.fxml", "Gestión de Empleados");
    }
    
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 