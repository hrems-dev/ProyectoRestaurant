package pe.edu.upeu.proyecto_restaurant.control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.proyecto_restaurant.modulo.Empleado;
import pe.edu.upeu.proyecto_restaurant.service.EmpleadoService;

@Controller
public class ControlEmpleado implements Initializable {
    
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cboRol;
    @FXML private ComboBox<String> cboEstado;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, Long> colId;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colApellido;
    @FXML private TableColumn<Empleado, String> colDni;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colUsuario;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colEstado;
    @FXML private TableColumn<Empleado, Void> colAcciones;

    @Autowired
    private EmpleadoService empleadoService;

    private ObservableList<Empleado> empleados;
    private Empleado empleadoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empleados = FXCollections.observableArrayList();
        
        // Initialize ComboBoxes
        cboRol.setItems(FXCollections.observableArrayList("admin", "cajero"));
        cboEstado.setItems(FXCollections.observableArrayList("ACTIVO", "INACTIVO"));
        
        // Initialize TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Add action buttons to table
        setupAccionesColumn();
        
        // Load initial data
        cargarEmpleados();
        
        // Setup button handlers
        btnGuardar.setOnAction(e -> guardarEmpleado());
        btnLimpiar.setOnAction(e -> limpiarFormulario());

        // Configurar el listener de selección de la tabla
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            empleadoSeleccionado = newSelection;
            if (newSelection != null) {
                cargarEmpleadoEnFormulario(newSelection);
            }
        });
    }

    private void setupAccionesColumn() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(5, btnEditar, btnEliminar);

            {
                // Configurar estilos de los botones
                btnEditar.getStyleClass().add("button-small");
                btnEliminar.getStyleClass().add("button-small");
                btnEliminar.getStyleClass().add("button-danger");
                
                box.setStyle("-fx-alignment: CENTER;");

                btnEditar.setOnAction(event -> {
                    empleadoSeleccionado = getTableView().getItems().get(getIndex());
                    cargarEmpleadoEnFormulario(empleadoSeleccionado);
                });

                btnEliminar.setOnAction(event -> {
                    Empleado empleado = getTableView().getItems().get(getIndex());
                    eliminarEmpleado(empleado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void cargarEmpleados() {
        empleados.clear();
        empleados.addAll(empleadoService.listarTodos());
        tblEmpleados.setItems(empleados);
    }

    private void guardarEmpleado() {
        if (validarFormulario()) {
            try {
                Empleado empleado = new Empleado();
                if (!txtId.getText().isEmpty()) {
                    empleado.setId(Long.parseLong(txtId.getText()));
                }
                empleado.setNombre(txtNombre.getText());
                empleado.setApellido(txtApellido.getText());
                empleado.setDni(txtDni.getText());
                empleado.setTelefono(txtTelefono.getText());
                empleado.setUsuario(txtUsuario.getText());
                empleado.setPassword(txtPassword.getText());
                empleado.setRol(cboRol.getValue());
                empleado.setEstado(cboEstado.getValue());

                if (empleadoSeleccionado != null) {
                    empleadoService.actualizar(empleado);
                    mostrarMensaje("Éxito", "Empleado actualizado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    empleadoService.crear(empleado);
                    mostrarMensaje("Éxito", "Empleado creado correctamente", Alert.AlertType.INFORMATION);
                }
                
                cargarEmpleados();
                limpiarFormulario();
            } catch (Exception e) {
                mostrarMensaje("Error", "Error al guardar el empleado: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void cargarEmpleadoEnFormulario(Empleado empleado) {
        txtId.setText(empleado.getId() != null ? empleado.getId().toString() : "");
        txtNombre.setText(empleado.getNombre());
        txtApellido.setText(empleado.getApellido());
        txtDni.setText(empleado.getDni());
        txtTelefono.setText(empleado.getTelefono());
        txtUsuario.setText(empleado.getUsuario());
        txtPassword.setText(empleado.getPassword());
        cboRol.setValue(empleado.getRol());
        cboEstado.setValue(empleado.getEstado());
    }

    private void eliminarEmpleado(Empleado empleado) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro que desea eliminar este empleado?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            empleadoService.eliminar(empleado.getId());
            cargarEmpleados();
            mostrarMensaje("Éxito", "Empleado eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void limpiarFormulario() {
        empleadoSeleccionado = null;
        txtId.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtDni.clear();
        txtTelefono.clear();
        txtUsuario.clear();
        txtPassword.clear();
        cboRol.setValue(null);
        cboEstado.setValue(null);
    }

    private boolean validarFormulario() {
        StringBuilder mensaje = new StringBuilder();

        if (txtNombre.getText().isEmpty()) {
            mensaje.append("El nombre es requerido\n");
        }
        if (txtApellido.getText().isEmpty()) {
            mensaje.append("El apellido es requerido\n");
        }
        if (txtDni.getText().isEmpty()) {
            mensaje.append("El DNI es requerido\n");
        }
        if (txtTelefono.getText().isEmpty()) {
            mensaje.append("El teléfono es requerido\n");
        }
        if (txtUsuario.getText().isEmpty()) {
            mensaje.append("El usuario es requerido\n");
        }
        if (txtPassword.getText().isEmpty()) {
            mensaje.append("La contraseña es requerida\n");
        }
        if (cboRol.getValue() == null) {
            mensaje.append("El rol es requerido\n");
        }
        if (cboEstado.getValue() == null) {
            mensaje.append("El estado es requerido\n");
        }

        if (mensaje.length() > 0) {
            mostrarMensaje("Error de validación", mensaje.toString(), Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
} 