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

    private IAutorDAO autorDAO;
    private ObservableList<Autor> listaAutores;

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
     * Carga los datos desde la BD y llena la tabla.
     */
    private void cargarDatos() {
        List<Autor> autores = autorDAO.obtenerTodos();
        if (autores != null) {
            listaAutores = FXCollections.observableArrayList(autores);
            tablaAutores.setItems(listaAutores);
        }
    }

    /**
     * Captura los datos del formulario (incluyendo el calendario) y los guarda.
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
     * Elimina el autor seleccionado de la tabla.
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
     * Limpia las cajas de texto y el calendario.
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}