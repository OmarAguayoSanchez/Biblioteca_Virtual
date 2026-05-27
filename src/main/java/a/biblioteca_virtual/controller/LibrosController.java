package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.ILibroDAO;
import a.biblioteca_virtual.dao.IAutorDAO;
import a.biblioteca_virtual.dao.IEditorialDAO; // <-- Importamos el DAO
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.Model.Autor;
import a.biblioteca_virtual.Model.Editorial; // <-- Importamos el Modelo
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class LibrosController {

    // Componentes de la Tabla
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Integer> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colDisponible;

    // Componentes del Formulario
    @FXML private TextField txtTitulo;
    @FXML private TextField txtIsbn;
    @FXML private Spinner<Integer> spnCantidad;

    // Menús desplegables para llaves foráneas
    @FXML private ComboBox<Autor> cbAutor;
    @FXML private ComboBox<Editorial> cbEditorial; // <-- Nuevo ComboBox

    private ILibroDAO libroDAO;
    private ObservableList<Libro> listaLibros;

    @FXML
    public void initialize() {
        // Inicializamos los DAOs desde la fábrica
        libroDAO = DAOFactory.getLibroDAO();
        IAutorDAO autorDAO = DAOFactory.getAutorDAO();
        IEditorialDAO editorialDAO = DAOFactory.getEditorialDAO(); // <-- Instanciamos

        // Configuramos las columnas para la tabla de libros
        colId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));

        // Configuramos el Spinner (Min: 1, Max: 100, Valor inicial: 1)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(valueFactory);

        // Cargamos la lista de autores en el ComboBox
        List<Autor> listaAutoresBD = autorDAO.obtenerTodos();
        if (listaAutoresBD != null) {
            cbAutor.setItems(FXCollections.observableArrayList(listaAutoresBD));
        }

        // Cargamos la lista de editoriales en el ComboBox <-- Llenado de datos
        List<Editorial> listaEditorialesBD = editorialDAO.obtenerTodos();
        if (listaEditorialesBD != null) {
            cbEditorial.setItems(FXCollections.observableArrayList(listaEditorialesBD));
        }

        cargarDatos();
    }

    private void cargarDatos() {
        List<Libro> librosBD = libroDAO.obtenerTodos();
        if (librosBD != null) {
            listaLibros = FXCollections.observableArrayList(librosBD);
            tablaLibros.setItems(listaLibros);
        }
    }

    @FXML
    private void guardarLibro(ActionEvent event) {
        // Validación de campos vacíos incluyendo ambos ComboBox
        if (txtTitulo.getText().isEmpty() || txtIsbn.getText().isEmpty() ||
                cbAutor.getValue() == null || cbEditorial.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor llena el título, ISBN, autor y editorial.");
            return;
        }

        Libro nuevoLibro = new Libro();
        nuevoLibro.setTitulo(txtTitulo.getText().trim());
        nuevoLibro.setIsbn(txtIsbn.getText().trim());
        nuevoLibro.setCantidadDisponible(spnCantidad.getValue());

        // Obtenemos los IDs reales de los objetos seleccionados
        Autor autorSeleccionado = cbAutor.getValue();
        nuevoLibro.setIdAutor(autorSeleccionado.getIdAutor());

        Editorial editorialSeleccionada = cbEditorial.getValue();
        nuevoLibro.setIdEditorial(editorialSeleccionada.getIdEditorial()); // <-- Asignación dinámica

        if (libroDAO.insertar(nuevoLibro)) {
            cargarDatos(); // Refrescar la tabla
            limpiarFormulario();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro guardado correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el libro. Revisa la consola para más detalles.");
        }
    }

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

    @FXML
    private void limpiarFormulario() {
        txtTitulo.clear();
        txtIsbn.clear();
        spnCantidad.getValueFactory().setValue(1);
        cbAutor.getSelectionModel().clearSelection();
        cbEditorial.getSelectionModel().clearSelection(); // <-- Limpia el ComboBox de Editorial
    }

    // Método auxiliar para mostrar ventanas emergentes ordenadas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}