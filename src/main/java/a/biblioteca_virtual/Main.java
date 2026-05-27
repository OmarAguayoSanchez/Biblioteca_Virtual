package a.biblioteca_virtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que arranca la aplicación de la Biblioteca Virtual.
 * * @author Omar Alejandro Aguayo Sanchez
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargamos el archivo FXML del Login
            // Asegúrate de que la ruta coincida con la ubicación de tu Login.fxml en la carpeta resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/a/biblioteca_virtual/Login.fxml"));
            Parent root = loader.load();

            // Configuramos la escena
            Scene scene = new Scene(root);

            // Configuramos la ventana (Stage)
            primaryStage.setTitle("Biblioteca Virtual - Acceso");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Evitamos que cambien el tamaño de la ventana de login
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Método que lanza la aplicación JavaFX
        launch(args);
    }
}
