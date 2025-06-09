package pe.edu.upeu.proyecto_restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;
import pe.edu.upeu.proyecto_restaurant.service.EmpleadoService;
//import pe.edu.upeu.sysventas.modelo.Marca;
//import pe.edu.upeu.sysventas.repositorio.EmisorRepository;
//import pe.edu.upeu.sysventas.repositorio.MarcaRepository;

@SpringBootApplication
public class ProyectoRestaurantApplication extends Application {
	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoRestaurantApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent = fxmlLoader.load();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
		scene.getStylesheets().add(getClass().getResource("/views/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Sistema de Gestión de Restaurante");
		stage.setResizable(true);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		configurableApplicationContext.close();
	}
}
