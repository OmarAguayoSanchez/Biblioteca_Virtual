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
 * Controlador principal del Dashboard.
 * Se encarga de manejar el menú lateral y cargar las vistas hijas
 * (Libros, Usuarios, Préstamos) dentro del área central.
 */
public class MainController {

    // El área central donde se cargarán las otras pantallas
    @FXML private StackPane contentArea;

    @FXML
    private void mostrarDevoluciones(ActionEvent event) {cargarVista("DevolucionesView.fxml");}
    @FXML
    private void mostrarLibros(ActionEvent event) {
        cargarVista("LibrosView.fxml");
    }

    @FXML
    private void mostrarUsuarios(ActionEvent event) {
        cargarVista("UsuariosView.fxml");
    }

    @FXML
    private void mostrarPrestamos(ActionEvent event) {
        cargarVista("PrestamosView.fxml");
    }

    @FXML
    private void mostrarAutores(javafx.event.ActionEvent event) {cargarVista("AutorView.fxml");}

    @FXML
    private void mostrarEditoriales(ActionEvent event) {cargarVista("EditorialView.fxml");}

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
     * Método auxiliar para cargar un archivo FXML dentro del StackPane central.
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
