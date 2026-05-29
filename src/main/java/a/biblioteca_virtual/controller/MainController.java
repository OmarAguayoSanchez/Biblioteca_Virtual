package a.biblioteca_virtual.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador principal del Dashboard de la aplicación.
 * Actúa como el contenedor maestro que gestiona la navegación del menú lateral.
 * Su función principal es interceptar los clics del menú y cargar dinámicamente
 * las vistas hijas (Libros, Usuarios, Préstamos.) dentro del área central
 * de la pantalla, manteniendo la barra de navegación lateral siempre visible.
 *
 * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class MainController {

    /** * Contenedor central de la interfaz gráfica.
     * Es el panel dinámico que se limpia y se repuebla cada vez que el usuario
     * selecciona una nueva opción en el menú lateral.
     */
    @FXML private StackPane contentArea;

    /**
     * Carga la vista de gestión de devoluciones en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarDevoluciones(ActionEvent event) {
        cargarVista("DevolucionesView.fxml");
    }

    /**
     * Carga la vista del catálogo de libros en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarLibros(ActionEvent event) {
        cargarVista("LibrosView.fxml");
    }

    /**
     * Carga la vista de gestión de usuarios en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarUsuarios(ActionEvent event) {
        cargarVista("UsuariosView.fxml");
    }

    /**
     * Carga la vista para registrar nuevos préstamos en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarPrestamos(ActionEvent event) {
        cargarVista("PrestamosView.fxml");
    }

    /**
     * Carga la vista de administración del catálogo de autores en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarAutores(ActionEvent event) {
        cargarVista("AutorView.fxml");
    }

    /**
     * Carga la vista de administración de las casas editoriales en el área central.
     *
     * @param event El evento disparado al hacer clic en el botón correspondiente del menú.
     */
    @FXML
    private void mostrarEditoriales(ActionEvent event) {
        cargarVista("EditorialView.fxml");
    }

    /**
     * Cierra la sesión activa del usuario actual.
     * Destruye la ventana principal (Dashboard) y vuelve a cargar e instanciar
     * la pantalla de inicio de sesión (Login).
     *
     * @param event El evento disparado al presionar el botón "Cerrar Sesión".
     */
    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            // Cerramos la ventana actual
            Stage stageActual = (Stage) contentArea.getScene().getWindow();
            stageActual.close();

            // Abrimos de nuevo el Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/a/biblioteca_virtual/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Biblioteca Virtual - Acceso");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar centralizado para inyectar archivos FXML en el panel principal.
     * Limpia cualquier contenido previo en el {@code contentArea} y carga el nuevo nodo raíz,
     * evitando así tener código duplicado de carga FXML en cada botón del menú.
     *
     * @param archivoFxml El nombre exacto del archivo FXML (incluyendo la extensión .fxml) a cargar.
     */
    private void cargarVista(String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/a/biblioteca_virtual/" + archivoFxml));
            Parent vista = loader.load();

            // Limpiamos lo que haya en el centro y agregamos la nueva vista
            contentArea.getChildren().clear();
            contentArea.getChildren().add(vista);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista " + archivoFxml + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
