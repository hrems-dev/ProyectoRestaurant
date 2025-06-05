package pe.edu.upeu.proyecto_restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
//import pe.edu.upeu.sysventas.modelo.Marca;
//import pe.edu.upeu.sysventas.repositorio.EmisorRepository;
//import pe.edu.upeu.sysventas.repositorio.MarcaRepository;

import java.util.List;

@SpringBootApplication
public class ProyectoRestaurantApplication extends Application{
	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;
	public static void main(String[] args) {
		//SpringApplication.run(ProyectoRestaurantApplication.class, args);
		launch(args);
	}
	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoRestaurantApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0])); // para
		//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_producto.fxml"));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admMenu.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent= fxmlLoader.load();
	}
	 @Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
		scene.getStylesheets().add(getClass().getResource("/views/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SysAlmacen Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	} 




}
