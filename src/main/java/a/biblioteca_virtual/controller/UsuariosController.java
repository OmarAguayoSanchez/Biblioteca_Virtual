package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IUsuarioDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador de la interfaz gráfica para el módulo de administración de Usuarios.
 * Esta clase se encarga de gestionar la vista donde los administradores pueden
 * registrar nuevos usuarios (clientes o administradores), visualizar el catálogo
 * completo y eliminar cuentas del sistema.
 * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class UsuariosController {

    // --- COMPONENTES DE LA TABLA ---

    /** Tabla principal que muestra todos los usuarios registrados en el sistema. */
    @FXML private TableView<Usuario> tablaUsuarios;

    /** Columna de la tabla que muestra el identificador único del usuario. */
    @FXML private TableColumn<Usuario, Integer> colId;

    /** Columna de la tabla que muestra el nombre completo del usuario. */
    @FXML private TableColumn<Usuario, String> colNombre;

    /** Columna de la tabla que muestra el correo electrónico. */
    @FXML private TableColumn<Usuario, String> colCorreo;

    /** Columna de la tabla que muestra el nombre de usuario (username) utilizado para el inicio de sesión. */
    @FXML private TableColumn<Usuario, String> colUsername;

    /** Columna de la tabla que muestra el nivel de permisos (rol) del usuario. */
    @FXML private TableColumn<Usuario, String> colRol;

    // --- COMPONENTES DEL FORMULARIO ---

    /** Campo de texto para ingresar el nombre completo del nuevo usuario. */
    @FXML private TextField txtNombre;

    /** Campo de texto para ingresar el correo electrónico. */
    @FXML private TextField txtCorreo;

    /** Campo de texto para asignar el nombre de usuario de la cuenta. */
    @FXML private TextField txtUsername;

    /** Campo de texto oculto para capturar de manera segura la contraseña del usuario. */
    @FXML private PasswordField txtPassword;

    /** Menú desplegable para seleccionar los privilegios de la cuenta (CLIENT o ADMIN). */
    @FXML private ComboBox<String> cbRol;

    /** Instancia del DAO para ejecutar las operaciones (CRUD) sobre la tabla de usuarios. */
    private IUsuarioDAO usuarioDAO;

    /**
     * Método invocado automáticamente por JavaFX tras cargar el archivo FXML.
     * Inicializa el DAO desde la fábrica, configura las columnas de la tabla vinculándolas
     * con los atributos del modelo {@link Usuario}, define los roles disponibles en el ComboBox
     * y realiza la primera carga de datos visuales.
     */
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

    /**
     * Recupera la lista completa de usuarios desde la base de datos a través del DAO
     * y actualiza el contenido observable de la tabla en la interfaz gráfica.
     */
    private void cargarDatos() {
        List<Usuario> usuariosBD = usuarioDAO.obtenerTodos();
        if (usuariosBD != null) {
            ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuariosBD);
            tablaUsuarios.setItems(listaUsuarios);
        }
    }

    /**
     * Captura los datos ingresados en el formulario, valida que no existan campos vacíos
     * y envía la solicitud al DAO para registrar la nueva cuenta en el sistema.
     * Notifica al usuario en caso de éxito o si existe un conflicto (ej. username o correo duplicado).
     *
     * @param event El evento de acción disparado al presionar el botón "Guardar".
     */
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

    /**
     * Identifica al usuario seleccionado actualmente en la tabla visual y solicita su
     * eliminación permanente de la base de datos.
     *
     * @param event El evento de acción disparado al presionar el botón de eliminar.
     */
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

    /**
     * Restablece todos los campos del formulario de registro, borrando el texto y
     * devolviendo el ComboBox de roles a su opción por defecto.
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtCorreo.clear();
        txtUsername.clear();
        txtPassword.clear();
        cbRol.getSelectionModel().selectFirst();
    }

    /**
     * Método de utilidad privado para mostrar ventanas emergentes de notificación.
     *
     * @param tipo    La categoría de la alerta.
     * @param titulo  El texto de cabecera de la ventana.
     * @param mensaje La instrucción o detalle específico que leerá el usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}