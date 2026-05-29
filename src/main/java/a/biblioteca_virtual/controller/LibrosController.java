package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.ILibroDAO;
import a.biblioteca_virtual.dao.IAutorDAO;
import a.biblioteca_virtual.dao.IEditorialDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.Model.Autor;
import a.biblioteca_virtual.Model.Editorial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador de la interfaz gráfica para la gestión del catálogo principal de Libros.
 * Actúa como intermediario entre la vista FXML y las capas de acceso a datos.
 * Integra información de múltiples entidades (Libros, Autores y Editoriales) para
 * permitir la creación y visualización completa de los ejemplares de la biblioteca.
 * * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class LibrosController {

    // --- COMPONENTES DE LA TABLA ---

    /** Tabla principal que muestra el catálogo de libros registrados. */
    @FXML private TableView<Libro> tablaLibros;

    /** Columna de la tabla que muestra el ID único del libro. */
    @FXML private TableColumn<Libro, Integer> colId;

    /** Columna de la tabla que muestra el título de la obra. */
    @FXML private TableColumn<Libro, String> colTitulo;

    /** Columna de la tabla que muestra el código ISBN del libro. */
    @FXML private TableColumn<Libro, String> colIsbn;

    /** Columna de la tabla que muestra el año de publicación. */
    @FXML private TableColumn<Libro, Integer> colAnio;

    /** Columna de la tabla que muestra el género literario. */
    @FXML private TableColumn<Libro, String> colGenero;

    /** Columna de la tabla que muestra la cantidad de ejemplares disponibles en inventario. */
    @FXML private TableColumn<Libro, Integer> colDisponible;

    // --- COMPONENTES DEL FORMULARIO ---

    /** Campo de texto para ingresar el título del libro. */
    @FXML private TextField txtTitulo;

    /** Campo de texto para ingresar el ISBN. */
    @FXML private TextField txtIsbn;

    /** Campo de texto para ingresar el año en que se publicó la obra. */
    @FXML private TextField txtAnio;

    /** Campo de texto para categorizar el género literario del libro. */
    @FXML private TextField txtGenero;

    /** Control numérico (Spinner) para definir la cantidad de copias físicas disponibles. */
    @FXML private Spinner<Integer> spnCantidad;

    /** Menú desplegable para seleccionar al autor de la obra. */
    @FXML private ComboBox<Autor> cbAutor;

    /** Menú desplegable para seleccionar la editorial que publica el libro. */
    @FXML private ComboBox<Editorial> cbEditorial;

    /** Instancia del DAO principal para ejecutar las operaciones (CRUD) sobre los libros. */
    private ILibroDAO libroDAO;

    /** Lista observable de JavaFX que enlaza los libros cargados en memoria con la tabla visual. */
    private ObservableList<Libro> listaLibros;

    /**
     * Método invocado automáticamente por JavaFX tras cargar el archivo FXML.
     * Inicializa las dependencias (DAOs), enlaza las columnas de la tabla con el modelo {@link Libro},
     * configura los valores permitidos en el Spinner de cantidades, y puebla los menús
     * desplegables con los datos existentes de Autores y Editoriales.
     */
    @FXML
    public void initialize() {
        libroDAO = DAOFactory.getLibroDAO();
        IAutorDAO autorDAO = DAOFactory.getAutorDAO();
        IEditorialDAO editorialDAO = DAOFactory.getEditorialDAO();

        // Configuración de las columnas (Asegúrate de que los nombres coincidan con Libro.java)
        colId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        // VINCULAMOS LAS NUEVAS COLUMNAS
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        colDisponible.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(valueFactory);

        // Llenar ComboBoxes
        List<Autor> listaAutoresBD = autorDAO.obtenerTodos();
        if (listaAutoresBD != null) cbAutor.setItems(FXCollections.observableArrayList(listaAutoresBD));

        List<Editorial> listaEditorialesBD = editorialDAO.obtenerTodos();
        if (listaEditorialesBD != null) cbEditorial.setItems(FXCollections.observableArrayList(listaEditorialesBD));

        cargarDatos();
    }

    /**
     * Consulta la base de datos a través del DAO de libros para obtener el catálogo completo
     * y actualiza la lista observable, lo que refresca automáticamente la tabla en pantalla.
     */
    private void cargarDatos() {
        List<Libro> librosBD = libroDAO.obtenerTodos();
        if (librosBD != null) {
            listaLibros = FXCollections.observableArrayList(librosBD);
            tablaLibros.setItems(listaLibros);
        }
    }

    /**
     * Captura la información del formulario, realiza validaciones (campos vacíos y formato numérico),
     * ensambla un nuevo objeto {@link Libro} rescatando las llaves foráneas de los ComboBox,
     * y solicita su inserción en la base de datos.
     *
     * @param event El evento de acción disparado al presionar el botón "Guardar".
     */
    @FXML
    private void guardarLibro(ActionEvent event) {
        // Validar que no haya campos vacíos
        if (txtTitulo.getText().isEmpty() || txtIsbn.getText().isEmpty() ||
                txtAnio.getText().isEmpty() || txtGenero.getText().isEmpty() ||
                cbAutor.getValue() == null || cbEditorial.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor llena todos los campos del libro.");
            return;
        }

        int anioPub = 0;
        try {
            anioPub = Integer.parseInt(txtAnio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "El año de publicación debe ser un número entero válido.");
            return;
        }

        Libro nuevoLibro = new Libro();
        nuevoLibro.setTitulo(txtTitulo.getText().trim());
        nuevoLibro.setIsbn(txtIsbn.getText().trim());
        nuevoLibro.setAnioPublicacion(anioPub); // Guardamos el año
        nuevoLibro.setGenero(txtGenero.getText().trim()); // Guardamos el género
        nuevoLibro.setCantidadDisponible(spnCantidad.getValue());

        nuevoLibro.setIdAutor(cbAutor.getValue().getIdAutor());
        nuevoLibro.setIdEditorial(cbEditorial.getValue().getIdEditorial());

        if (libroDAO.insertar(nuevoLibro)) {
            cargarDatos();
            limpiarFormulario();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro guardado correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el libro.");
        }
    }

    /**
     * Identifica el libro seleccionado en la tabla visual y solicita su eliminación permanente.
     * Advierte al usuario si no hay ninguna selección o si el borrado falla por
     * restricciones de llave foránea (ej. el libro está prestado).
     *
     * @param event El evento de acción disparado al presionar el botón de eliminar.
     */
    @FXML
    private void eliminarLibro(ActionEvent event) {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            if (libroDAO.eliminar(libroSeleccionado.getIdLibro())) {
                cargarDatos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el libro.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor selecciona un libro de la tabla.");
        }
    }

    /**
     * Restablece todos los controles del formulario a su estado original, dejándolos
     * preparados para ingresar un nuevo libro.
     */
    @FXML
    private void limpiarFormulario() {
        txtTitulo.clear();
        txtIsbn.clear();
        txtAnio.clear(); // Limpiar nuevo campo
        txtGenero.clear(); // Limpiar nuevo campo
        spnCantidad.getValueFactory().setValue(1);
        cbAutor.getSelectionModel().clearSelection();
        cbEditorial.getSelectionModel().clearSelection();
    }

    /**
     * Método de utilidad privado para mostrar ventanas emergentes de notificación.
     *
     * @param tipo    La categoría de la alerta.
     * @param titulo  El texto principal que aparecerá como cabecera del cuadro de diálogo.
     * @param mensaje La descripción o instrucción detallada que leerá el usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}