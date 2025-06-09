package pe.edu.upeu.proyecto_restaurant.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pe.edu.upeu.proyecto_restaurant.ProyectoRestaurantApplication;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;

/**
 * Utility class to manage JavaFX stages and employee session
 */
public class StageManager {
    private static Stage primaryStage;
    private static Empleado currentEmpleado;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Empleado getCurrentEmpleado() {
        return currentEmpleado;
    }

    public static void setCurrentEmpleado(Empleado empleado) {
        currentEmpleado = empleado;
    }

    public static void clearSession() {
        currentEmpleado = null;
    }

    public static void mostrarLogin() {
        try {
            // Cargar el FXML del login
            FXMLLoader loader = new FXMLLoader(ProyectoRestaurantApplication.class.getResource("/views/login.fxml"));
            Parent root = loader.load();
            
            // Crear una nueva escena
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ProyectoRestaurantApplication.class.getResource("/css/styles.css").toExternalForm());
            
            // Configurar y mostrar el stage
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Sistema Restaurante - Login");
            stage.setResizable(false);
            stage.show();
            
            // Guardar referencia del stage
            setPrimaryStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 