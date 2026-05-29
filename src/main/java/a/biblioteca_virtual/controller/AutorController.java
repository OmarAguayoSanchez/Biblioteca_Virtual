package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IAutorDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Autor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de la interfaz gráfica para la gestión del catálogo de Autores.
 * Esta clase actúa como intermediario entre la vista gráfica (FXML) y la capa
 * de acceso a datos (DAO). Se encarga de manejar los eventos del usuario,
 * validar el formulario y mantener sincronizada la tabla visual con la base de datos.
 * * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class AutorController {

    // --- COMPONENTES DEL FORMULARIO ---
    @FXML private TextField txtNombre;
    @FXML private TextField txtNacionalidad;
    @FXML private DatePicker dpFechaNacimiento;

    // --- COMPONENTES DE LA TABLA ---
    @FXML private TableView<Autor> tablaAutores;
    @FXML private TableColumn<Autor, Integer> colId;
    @FXML private TableColumn<Autor, String> colNombre;
    @FXML private TableColumn<Autor, String> colNacionalidad;
    @FXML private TableColumn<Autor, LocalDate> colFechaNacimiento;

    /** Instancia del DAO para ejecutar las operaciones (CRUD) en la tabla Autores de la base de datos. */
    private IAutorDAO autorDAO;

    /** Lista observable de JavaFX que permite actualizar la tabla gráficamente cuando los datos cambian. */
    private ObservableList<Autor> listaAutores;

    /**
     * Método invocado automáticamente por JavaFX una vez que el archivo FXML ha sido cargado.
     * Se encarga de inicializar el DAO desde la fábrica, configurar cómo cada columna
     * debe leer los atributos de la clase {@link Autor} y realizar la primera carga de datos.
     */
    @FXML
    public void initialize() {
        // Inicializamos el DAO desde tu fábrica
        autorDAO = DAOFactory.getAutorDAO();

        // Vinculamos las columnas con los nombres EXACTOS de los atributos en tu clase Autor
        colId.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        cargarDatos();
    }

    /**
     * Recupera la lista completa de autores desde la base de datos utilizando el DAO
     * y actualiza la información mostrada en la tabla visual.
     */
    private void cargarDatos() {
        List<Autor> autores = autorDAO.obtenerTodos();
        if (autores != null) {
            listaAutores = FXCollections.observableArrayList(autores);
            tablaAutores.setItems(listaAutores);
        }
    }

    /**
     * Captura los datos ingresados por el usuario en los campos de texto y el calendario,
     * valida la información, crea una nueva instancia de {@link Autor} y solicita al DAO
     * que la guarde en la base de datos.
     *
     * @param event El evento de acción disparado al presionar el botón "Guardar".
     */
    @FXML
    private void guardarAutor(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        LocalDate fecha = dpFechaNacimiento.getValue();

        // Validación básica: al menos el nombre debe ser obligatorio
        if (nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "El nombre del autor es obligatorio.");
            return;
        }

        // Usamos el constructor vacío y los setters de tu nuevo modelo
        Autor nuevoAutor = new Autor();
        nuevoAutor.setNombre(nombre);
        nuevoAutor.setNacionalidad(nacionalidad);
        nuevoAutor.setFechaNacimiento(fecha);

        if (autorDAO.insertar(nuevoAutor)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Autor registrado correctamente.");
            cargarDatos();
            limpiarFormulario();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el autor en la base de datos.");
        }
    }

    /**
     * Identifica el autor actualmente seleccionado en la tabla y solicita al DAO
     * su eliminación permanente de la base de datos.
     * Si el autor tiene libros asociados, la restricción de llave foránea impedirá el borrado.
     *
     * @param event El evento de acción disparado al presionar el botón de eliminar.
     */
    @FXML
    private void eliminarAutor(ActionEvent event) {
        Autor seleccionado = tablaAutores.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            if (autorDAO.eliminar(seleccionado.getIdAutor())) {
                cargarDatos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el autor. Verifica si tiene libros registrados.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor selecciona un autor de la tabla.");
        }
    }

    /**
     * Restablece todos los controles del formulario (campos de texto y selectores de fecha)
     * a su estado inicial, dejándolos vacíos para un nuevo registro.
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null);
    }

    /**
     * Método de utilidad para generar y mostrar cuadros de diálogo en la pantalla.
     *
     * @param tipo    El tipo de alerta.
     * @param titulo  El texto que aparecerá en la barra de título de la ventana.
     * @param mensaje La descripción detallada del evento que se mostrará al usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}