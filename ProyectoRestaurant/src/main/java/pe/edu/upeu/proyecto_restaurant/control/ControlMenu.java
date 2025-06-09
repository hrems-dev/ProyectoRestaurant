package pe.edu.upeu.proyecto_restaurant.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.proyecto_restaurant.modulo.Producto;
import pe.edu.upeu.proyecto_restaurant.modulo.Categoria;
import pe.edu.upeu.proyecto_restaurant.modulo.Seccion;
import pe.edu.upeu.proyecto_restaurant.service.ProductoService;
import pe.edu.upeu.proyecto_restaurant.service.CategoriaService;
import pe.edu.upeu.proyecto_restaurant.service.SeccionService;
import pe.edu.upeu.proyecto_restaurant.utils.StageManager;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Optional;
import java.util.List;
import javafx.event.ActionEvent;

@Controller
public class ControlMenu implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(ControlMenu.class);
    
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Categoria> cboCategoria;
    @FXML private ComboBox<Seccion> cboSeccion;
    @FXML private ComboBox<String> cboEstado;
    @FXML private TextField txtImagen;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, Long> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colSeccion;
    @FXML private TableColumn<Producto, String> colEstado;
    @FXML private TableColumn<Producto, Void> colAcciones;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SeccionService seccionService;

    private ObservableList<Producto> productos;
    private Producto productoSeleccionado;
    private Map<String, Categoria> categoriasMap;
    private Map<String, Seccion> seccionesMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            logger.info("Inicializando ControlMenu");
            
            productos = FXCollections.observableArrayList();
            categoriasMap = new HashMap<>();
            seccionesMap = new HashMap<>();
            
            // Inicializar ComboBoxes
            cboEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
            
            // Inicializar columnas de la tabla
            colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            colCategoria.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getCategoria() != null ? 
                    cellData.getValue().getCategoria().getNombre() : ""));
            colSeccion.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getSeccion() != null ? 
                    cellData.getValue().getSeccion().getNombre() : ""));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            
            // Configurar botones de acción
            setupAccionesColumn();
            
            // Cargar datos iniciales
            cargarCategorias();
            cargarSecciones();
            cargarProductos();
            
            // Configurar manejadores de eventos
            btnGuardar.setOnAction(e -> handleGuardar());
            btnLimpiar.setOnAction(e -> handleLimpiar());
            btnSeleccionarImagen.setOnAction(e -> handleSeleccionarImagen());

            // Configurar listener de selección de la tabla
            tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                productoSeleccionado = newSelection;
                if (newSelection != null) {
                    cargarProductoEnFormulario(newSelection);
                }
            });
        } catch (Exception e) {
            logger.error("Error al inicializar ControlMenu", e);
            mostrarMensaje("Error", "Error al inicializar la pantalla: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.listarTodos();
            if (categorias.isEmpty()) {
                logger.warn("No se encontraron categorías en la base de datos");
                mostrarMensaje("Advertencia", "No hay categorías disponibles. Debe crear al menos una categoría.", Alert.AlertType.WARNING);
                return;
            }
            cboCategoria.setItems(FXCollections.observableArrayList(categorias));
            logger.info("Categorías cargadas correctamente. Total: {}", categorias.size());
        } catch (Exception e) {
            logger.error("Error al cargar categorías", e);
            mostrarMensaje("Error", "Error al cargar las categorías: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarSecciones() {
        try {
            List<Seccion> secciones = seccionService.listarTodos();
            if (secciones.isEmpty()) {
                logger.warn("No se encontraron secciones en la base de datos");
                mostrarMensaje("Advertencia", "No hay secciones disponibles. Debe crear al menos una sección.", Alert.AlertType.WARNING);
                return;
            }
            cboSeccion.setItems(FXCollections.observableArrayList(secciones));
            logger.info("Secciones cargadas correctamente. Total: {}", secciones.size());
        } catch (Exception e) {
            logger.error("Error al cargar secciones", e);
            mostrarMensaje("Error", "Error al cargar las secciones: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupAccionesColumn() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.getStyleClass().add("button-small");
                btnEliminar.getStyleClass().add("button-small");
                btnEliminar.getStyleClass().add("button-danger");
                
                box.setStyle("-fx-alignment: CENTER;");

                btnEditar.setOnAction(event -> {
                    productoSeleccionado = getTableView().getItems().get(getIndex());
                    cargarProductoEnFormulario(productoSeleccionado);
                });

                btnEliminar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminarProducto(producto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void cargarProductos() {
        try {
            productos.clear();
            productos.addAll(productoService.listarProductos());
            tblProductos.setItems(productos);
        } catch (Exception e) {
            logger.error("Error al cargar productos", e);
            mostrarMensaje("Error", "Error al cargar los productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void guardarProducto() {
        if (validarFormulario()) {
            try {
                String categoriaSeleccionada = cboCategoria.getValue().getNombre();
                String seccionSeleccionada = cboSeccion.getValue().getNombre();
                
                // Obtener y validar la categoría
                Categoria categoria = categoriasMap.get(categoriaSeleccionada);
                if (categoria == null) {
                    mostrarMensaje("Error", "No se pudo obtener la categoría seleccionada. Por favor, recargue la página e intente nuevamente.", Alert.AlertType.ERROR);
                    return;
                }
                
                // Obtener y validar la sección
                Seccion seccion = seccionesMap.get(seccionSeleccionada);
                if (seccion == null) {
                    mostrarMensaje("Error", "No se pudo obtener la sección seleccionada. Por favor, recargue la página e intente nuevamente.", Alert.AlertType.ERROR);
                    return;
                }

                // Crear o actualizar el producto
                Producto producto = new Producto();
                if (!txtId.getText().isEmpty()) {
                    try {
                        producto.setIdProducto(Long.parseLong(txtId.getText().trim()));
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID del producto", e);
                        mostrarMensaje("Error", "El ID del producto no es válido", Alert.AlertType.ERROR);
                        return;
                    }
                }

                // Establecer los valores del producto
                producto.setNombre(txtNombre.getText().trim());
                producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                producto.setStock(Integer.parseInt(txtStock.getText().trim()));
                producto.setDescripcion(txtDescripcion.getText().trim());
                producto.setCategoria(categoria);
                producto.setSeccion(seccion);
                producto.setEstado(cboEstado.getValue());
                producto.setImagen(txtImagen.getText().trim());

                // Guardar o actualizar el producto
                if (productoSeleccionado != null) {
                    producto.setIdProducto(productoSeleccionado.getIdProducto());
                    productoService.actualizarProducto(producto);
                    logger.info("Producto actualizado correctamente: {}", producto.getNombre());
                    mostrarMensaje("Éxito", "Producto actualizado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    productoService.guardarProducto(producto);
                    logger.info("Producto creado correctamente: {}", producto.getNombre());
                    mostrarMensaje("Éxito", "Producto creado correctamente", Alert.AlertType.INFORMATION);
                }
                
                // Recargar la tabla y limpiar el formulario
                cargarProductos();
                limpiarFormulario();
            } catch (NumberFormatException e) {
                logger.error("Error al convertir valores numéricos", e);
                mostrarMensaje("Error", "Por favor, verifique los valores numéricos ingresados", Alert.AlertType.ERROR);
            } catch (Exception e) {
                logger.error("Error al guardar el producto", e);
                mostrarMensaje("Error", "Error al guardar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void cargarProductoEnFormulario(Producto producto) {
        if (producto != null) {
            txtId.setText(producto.getIdProducto() != null ? producto.getIdProducto().toString() : "");
            txtNombre.setText(producto.getNombre() != null ? producto.getNombre() : "");
            txtPrecio.setText(producto.getPrecio() != null ? producto.getPrecio().toString() : "");
            txtStock.setText(producto.getStock() != null ? producto.getStock().toString() : "");
            txtDescripcion.setText(producto.getDescripcion() != null ? producto.getDescripcion() : "");
            
            if (producto.getCategoria() != null) {
                cboCategoria.setValue(producto.getCategoria());
            } else {
                cboCategoria.setValue(null);
            }
            
            if (producto.getSeccion() != null) {
                cboSeccion.setValue(producto.getSeccion());
            } else {
                cboSeccion.setValue(null);
            }
            
            cboEstado.setValue(producto.getEstado());
            txtImagen.setText(producto.getImagen() != null ? producto.getImagen() : "");
        } else {
            limpiarFormulario();
        }
    }

    private void eliminarProducto(Producto producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro que desea eliminar este producto?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            productoService.eliminarProducto(producto.getIdProducto());
            cargarProductos();
            mostrarMensaje("Éxito", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void limpiarFormulario() {
        productoSeleccionado = null;
        txtId.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtDescripcion.clear();
        cboCategoria.setValue(null);
        cboSeccion.setValue(null);
        cboEstado.setValue(null);
        txtImagen.clear();
    }

    @FXML
    private void handleSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txtImagen.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleGuardar() {
        guardarProducto();
    }

    private boolean validarFormulario() {
        StringBuilder mensaje = new StringBuilder();

        // Validación de campos básicos
        if (txtNombre.getText().trim().isEmpty()) {
            mensaje.append("El nombre es requerido\n");
        }
        
        // Validación del precio
        if (txtPrecio.getText().trim().isEmpty()) {
            mensaje.append("El precio es requerido\n");
        } else {
            try {
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                if (precio <= 0) {
                    mensaje.append("El precio debe ser mayor a 0\n");
                }
            } catch (NumberFormatException e) {
                mensaje.append("El precio debe ser un número válido\n");
            }
        }
        
        // Validación del stock
        if (txtStock.getText().trim().isEmpty()) {
            mensaje.append("El stock es requerido\n");
        } else {
            try {
                int stock = Integer.parseInt(txtStock.getText().trim());
                if (stock < 0) {
                    mensaje.append("El stock no puede ser negativo\n");
                }
            } catch (NumberFormatException e) {
                mensaje.append("El stock debe ser un número entero\n");
            }
        }

        // Validación de categoría
        if (cboCategoria.getValue() == null || cboCategoria.getValue().getNombre() == null) {
            mensaje.append("La categoría es requerida\n");
        } else if (!categoriasMap.containsKey(cboCategoria.getValue().getNombre())) {
            mensaje.append("La categoría seleccionada no es válida\n");
        } else {
            Categoria categoria = categoriasMap.get(cboCategoria.getValue().getNombre());
            if (categoria == null) {
                mensaje.append("Error al obtener la categoría seleccionada\n");
            }
        }

        // Validación de sección
        if (cboSeccion.getValue() == null || cboSeccion.getValue().getNombre() == null) {
            mensaje.append("La sección es requerida\n");
        } else if (!seccionesMap.containsKey(cboSeccion.getValue().getNombre())) {
            mensaje.append("La sección seleccionada no es válida\n");
        } else {
            Seccion seccion = seccionesMap.get(cboSeccion.getValue().getNombre());
            if (seccion == null) {
                mensaje.append("Error al obtener la sección seleccionada\n");
            }
        }

        // Validación de estado
        if (cboEstado.getValue() == null || cboEstado.getValue().trim().isEmpty()) {
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

    @FXML
    private void handleNuevaCategoria() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Categoría");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre de la nueva categoría:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            try {
                if (nombre.trim().isEmpty()) {
                    mostrarMensaje("Error", "El nombre de la categoría no puede estar vacío", Alert.AlertType.ERROR);
                    return;
                }

                // Verificar si ya existe una categoría con ese nombre
                if (categoriaService.buscarPorNombre(nombre) != null) {
                    mostrarMensaje("Error", "Ya existe una categoría con ese nombre", Alert.AlertType.ERROR);
                    return;
                }

                // Crear nueva categoría
                Categoria nuevaCategoria = new Categoria();
                nuevaCategoria.setNombre(nombre);
                nuevaCategoria.setEstado("Activo");
                
                // Guardar la categoría
                Categoria categoriaGuardada = categoriaService.guardarCategoria(nuevaCategoria);
                
                // Actualizar el ComboBox
                cargarCategorias();
                
                // Seleccionar la nueva categoría
                cboCategoria.setValue(categoriaGuardada);
                
                mostrarMensaje("Éxito", "Categoría creada correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                logger.error("Error al crear nueva categoría", e);
                mostrarMensaje("Error", "Error al crear la categoría: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleNuevaSeccion() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Sección");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre de la nueva sección:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            try {
                if (nombre.trim().isEmpty()) {
                    mostrarMensaje("Error", "El nombre de la sección no puede estar vacío", Alert.AlertType.ERROR);
                    return;
                }

                // Verificar si ya existe una sección con ese nombre
                if (seccionService.buscarPorNombre(nombre) != null) {
                    mostrarMensaje("Error", "Ya existe una sección con ese nombre", Alert.AlertType.ERROR);
                    return;
                }

                // Crear nueva sección
                Seccion nuevaSeccion = new Seccion();
                nuevaSeccion.setNombre(nombre);
                nuevaSeccion.setEstado("Activo");
                
                // Guardar la sección
                Seccion seccionGuardada = seccionService.guardar(nuevaSeccion);
                
                // Actualizar el ComboBox
                cargarSecciones();
                
                // Seleccionar la nueva sección
                cboSeccion.setValue(seccionGuardada);
                
                mostrarMensaje("Éxito", "Sección creada correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                logger.error("Error al crear nueva sección", e);
                mostrarMensaje("Error", "Error al crear la sección: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Button btn = (Button) event.getSource();
        Producto producto = (Producto) btn.getUserData();
        if (producto != null) {
            cargarProductoEnFormulario(producto);
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Button btn = (Button) event.getSource();
        Producto producto = (Producto) btn.getUserData();
        if (producto != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro que desea eliminar el producto " + producto.getNombre() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    productoService.eliminarProducto(producto.getIdProducto());
                    cargarProductos();
                    mostrarMensaje("Éxito", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    logger.error("Error al eliminar producto", e);
                    mostrarMensaje("Error", "Error al eliminar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }
} 