package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IUsuarioDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana del Login y Registro.
 * Maneja la lógica de interfaz de usuario para autenticar usuarios existentes
 * y registrar nuevos usuarios en la base de datos.
 *
 *  @author Omar Alejandro Aguayo Sanchez
 */
public class LoginController {

    // --- CONSTANTES ---
    private static final int MIN_LONGITUD_PASSWORD = 6;
    private static final String ROL_ADMIN = "ADMIN";
    private static final String RUTA_MAIN_VIEW = "/a/diccionario_examen/MainView.fxml";

    // --- VARIABLES DE LA PESTAÑA: REGISTRO ---
    @FXML private TextField txtNombreReg;
    @FXML private TextField txtCorreoReg;
    @FXML private TextField txtUsuarioReg;
    @FXML private PasswordField txtPasswordReg;
    @FXML private ComboBox<String> cmbRolReg;

    // --- VARIABLES DE LA PESTAÑA: LOGIN ---
    @FXML private TextField txtUsuarioLogin;
    @FXML private PasswordField txtPasswordLogin;

    /**
     * Objeto para acceder a los datos de los usuarios atraves del DAO.
     */
    private IUsuarioDAO usuarioDAO;

    /**
     * Método para inicializar la view.
     * Inicializa las dependencias y configura los valores de la vista.
     */
    @FXML
    public void initialize() {
        this.usuarioDAO = DAOFactory.getUsuarioDAO();

        if (cmbRolReg != null) {
            cmbRolReg.getSelectionModel().selectFirst();
        }
    }

    /**
     * Captura los datos del formulario de registro y solicita la creación
     * de un nuevo usuario en la base de datos tras validar los campos.
     */
    @FXML
    private void manejarRegistro() {
        String nombre = txtNombreReg.getText().trim();
        String correo = txtCorreoReg.getText().trim();
        String usuario = txtUsuarioReg.getText().trim();
        String password = txtPasswordReg.getText().trim();
        String rol = cmbRolReg.getValue();

        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, llena todos los campos para registrarte.", Alert.AlertType.WARNING);
            return;
        }

        if (password.length() < MIN_LONGITUD_PASSWORD) {
            mostrarAlerta("Contraseña Débil", "La contraseña debe tener al menos " + MIN_LONGITUD_PASSWORD + " caracteres.", Alert.AlertType.WARNING);
            return;
        }

        if (usuarioDAO.registrar(nombre, correo, usuario, password, rol)) {
            mostrarAlerta("Registro Exitoso", "El usuario " + usuario + " ha sido creado. Ve a la pestaña de Login para entrar.", Alert.AlertType.INFORMATION);
            limpiarFormularioRegistro();
        } else {
            mostrarAlerta("Error de Registro", "No se pudo crear el usuario. Es posible que el nombre de usuario o el correo ya estén en uso.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Captura los datos ingresadas por el usuario y verifica su validez
     * para permitir o negar el acceso al sistema.
     */
    @FXML
    private void manejarLogin() {
        String usuario = txtUsuarioLogin.getText().trim();
        String password = txtPasswordLogin.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Aviso", "Por favor ingresa tu usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        String rolUsuario = usuarioDAO.autenticar(usuario, password);

        if (rolUsuario != null) {
            Stage stageActual = (Stage) txtUsuarioLogin.getScene().getWindow();
            stageActual.close();

            abrirPantallaPrincipal(rolUsuario);
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            txtPasswordLogin.clear();
        }
    }

    /**
     * Carga y muestra la pantalla principal de la aplicación.
     * Configura el título de la ventana dependiendo del rol del usuario autenticado.
     *
     * @param rol El rol del usuario que acaba de iniciar sesión ("ADMIN" o "CLIENT").
     */
    private void abrirPantallaPrincipal(String rol) {
        try {
            String titulo = ROL_ADMIN.equals(rol)
                    ? "Diccionario Seguro - Modo Administrador"
                    : "Diccionario Seguro - Modo Cliente";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(RUTA_MAIN_VIEW));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage nuevoStage = new Stage();
            nuevoStage.setTitle(titulo);
            nuevoStage.setScene(scene);
            nuevoStage.show();

        } catch (Exception e) {
            System.err.println("Error al cargar la pantalla: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error Crítico", "No se pudo cargar la interfaz del sistema.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Limpia todos los campos de texto
     */
    private void limpiarFormularioRegistro() {
        txtNombreReg.clear();
        txtCorreoReg.clear();
        txtUsuarioReg.clear();
        txtPasswordReg.clear();
        cmbRolReg.getSelectionModel().selectFirst();
    }

    /**
     * Método para mostrar pestañas de diálogo de alertas en pantalla.
     *
     * @param titulo  El título de la ventana de alerta.
     * @param mensaje El contenido o mensaje detallado a mostrar.
     * @param tipo    El tipo de alerta (WARNING, ERROR, INFORMATION, etc.).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}