package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IEditorialDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Editorial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador de la interfaz gráfica para la gestión del catálogo de Editoriales.
 * Esta clase actúa como intermediario entre la vista FXML y la capa de acceso a datos (DAO).
 * Se encarga de procesar las acciones del usuario, validar el formulario y mantener
 * la tabla visual sincronizada con la base de datos.
 * * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class EditorialController {

    // --- COMPONENTES DEL FORMULARIO ---

    /** Campo de texto para ingresar el nombre oficial de la editorial. */
    @FXML private TextField txtNombre;

    /** Campo de texto para ingresar el país de origen de la editorial. */
    @FXML private TextField txtPais;

    /** Campo de texto para ingresar el teléfono de contacto de la editorial. */
    @FXML private TextField txtTelefono;

    // --- COMPONENTES DE LA TABLA ---

    /** Tabla principal que muestra el catálogo completo de editoriales registradas. */
    @FXML private TableView<Editorial> tablaEditoriales;

    /** Columna de la tabla que muestra el identificador único de la editorial. */
    @FXML private TableColumn<Editorial, Integer> colId;

    /** Columna de la tabla que muestra el nombre de la editorial. */
    @FXML private TableColumn<Editorial, String> colNombre;

    /** Columna de la tabla que muestra el país de la editorial. */
    @FXML private TableColumn<Editorial, String> colPais;

    /** Columna de la tabla que muestra el teléfono de la editorial. */
    @FXML private TableColumn<Editorial, String> colTelefono;

    /** Instancia del DAO para ejecutar las operaciones (CRUD) en la tabla Editoriales. */
    private IEditorialDAO editorialDAO;

    /** Lista observable de JavaFX que enlaza los datos en memoria con la tabla visual. */
    private ObservableList<Editorial> listaEditoriales;

    /**
     * Método invocado automáticamente por JavaFX tras cargar el archivo FXML.
     * Se encarga de obtener el DAO de la fábrica, configurar cómo las columnas de la tabla
     * deben leer los atributos del modelo {@link Editorial} y cargar los datos iniciales.
     */
    @FXML
    public void initialize() {
        // Inicializamos el DAO
        editorialDAO = DAOFactory.getEditorialDAO();

        // Vinculamos las columnas con los atributos exactos de tu clase Editorial
        colId.setCellValueFactory(new PropertyValueFactory<>("idEditorial"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        cargarDatos();
    }

    /**
     * Consulta la base de datos a través del DAO para obtener todas las editoriales
     * y actualiza la lista observable, lo que refresca automáticamente la tabla en pantalla.
     */
    private void cargarDatos() {
        List<Editorial> editoriales = editorialDAO.obtenerTodos();
        if (editoriales != null) {
            listaEditoriales = FXCollections.observableArrayList(editoriales);
            tablaEditoriales.setItems(listaEditoriales);
        }
    }

    /**
     * Captura la información escrita en los campos de texto del formulario, valida que
     * los datos requeridos no estén vacíos y solicita al DAO la inserción de una nueva editorial.
     *
     * @param event El evento de acción disparado al presionar el botón de guardar.
     */
    @FXML
    private void guardarEditorial(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String pais = txtPais.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "El nombre de la editorial es obligatorio.");
            return;
        }

        Editorial nuevaEditorial = new Editorial();
        nuevaEditorial.setNombre(nombre);
        nuevaEditorial.setPais(pais);
        nuevaEditorial.setTelefono(telefono);

        if (editorialDAO.insertar(nuevaEditorial)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Editorial registrada correctamente.");
            cargarDatos();
            limpiarFormulario();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la editorial en la base de datos.");
        }
    }

    /**
     * Identifica la editorial seleccionada actualmente en la tabla y solicita al DAO
     * su eliminación de la base de datos.
     * Advierte al usuario si la eliminación falla debido a restricciones (ej. libros asociados).
     *
     * @param event El evento de acción disparado al presionar el botón de eliminar.
     */
    @FXML
    private void eliminarEditorial(ActionEvent event) {
        Editorial seleccionada = tablaEditoriales.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            if (editorialDAO.eliminar(seleccionada.getIdEditorial())) {
                cargarDatos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la editorial. Verifica si tiene libros registrados.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor selecciona una editorial de la tabla.");
        }
    }

    /**
     * Limpia todo el contenido de los campos de texto del formulario para prepararlo
     * para la captura de un nuevo registro.
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtPais.clear();
        txtTelefono.clear();
    }

    /**
     * Método de utilidad privado para mostrar cuadros de diálogo en pantalla.
     *
     * @param tipo    El tipo de alerta visual (WARNING, ERROR, INFORMATION).
     * @param titulo  El texto de la cabecera de la ventana emergente.
     * @param mensaje El cuerpo del mensaje descriptivo que leerá el usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}