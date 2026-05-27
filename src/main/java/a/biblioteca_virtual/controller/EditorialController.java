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

public class EditorialController {

    // --- COMPONENTES DEL FORMULARIO ---
    @FXML private TextField txtNombre;
    @FXML private TextField txtPais;
    @FXML private TextField txtTelefono;

    // --- COMPONENTES DE LA TABLA ---
    @FXML private TableView<Editorial> tablaEditoriales;
    @FXML private TableColumn<Editorial, Integer> colId;
    @FXML private TableColumn<Editorial, String> colNombre;
    @FXML private TableColumn<Editorial, String> colPais;
    @FXML private TableColumn<Editorial, String> colTelefono;

    private IEditorialDAO editorialDAO;
    private ObservableList<Editorial> listaEditoriales;

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

    private void cargarDatos() {
        List<Editorial> editoriales = editorialDAO.obtenerTodos();
        if (editoriales != null) {
            listaEditoriales = FXCollections.observableArrayList(editoriales);
            tablaEditoriales.setItems(listaEditoriales);
        }
    }

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

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtPais.clear();
        txtTelefono.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}