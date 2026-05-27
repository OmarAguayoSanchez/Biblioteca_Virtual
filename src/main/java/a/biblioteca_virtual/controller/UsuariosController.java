package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IUsuarioDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Usuario; // <-- Importación exacta de tu modelo
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UsuariosController {

    // --- COMPONENTES DE LA TABLA ---
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colUsername;
    @FXML private TableColumn<Usuario, String> colRol;

    // --- COMPONENTES DEL FORMULARIO ---
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRol;

    private IUsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        usuarioDAO = DAOFactory.getUsuarioDAO();

        // ¡LA MAGIA DE LA TABLA!
        // Estos textos deben ser idénticos a los atributos de tu clase Usuario
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        cbRol.setItems(FXCollections.observableArrayList("CLIENT", "ADMIN"));
        cbRol.getSelectionModel().selectFirst();

        cargarDatos();
    }

    private void cargarDatos() {
        List<Usuario> usuariosBD = usuarioDAO.obtenerTodos();
        if (usuariosBD != null) {
            ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuariosBD);
            tablaUsuarios.setItems(listaUsuarios);
        }
    }

    @FXML
    private void guardarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String rol = cbRol.getValue();

        if (nombre.isEmpty() || correo.isEmpty() || username.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor llena todos los campos.");
            return;
        }

        boolean exito = usuarioDAO.registrar(nombre, correo, username, password, rol);

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado correctamente.");
            cargarDatos();
            limpiarFormulario();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el usuario. El correo o username ya existen.");
        }
    }

    @FXML
    private void eliminarUsuario(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            if (usuarioDAO.eliminar(seleccionado.getIdUsuario())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Usuario eliminado con éxito.");
                cargarDatos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el usuario en la base de datos.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Primero haz clic en un usuario de la tabla.");
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtCorreo.clear();
        txtUsername.clear();
        txtPassword.clear();
        cbRol.getSelectionModel().selectFirst();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}