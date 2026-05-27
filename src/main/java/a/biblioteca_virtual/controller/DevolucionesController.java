package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.IDetallePrestamoDAO;
import a.biblioteca_virtual.dao.ILibroDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.DetallePrestamo;
import a.biblioteca_virtual.Model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class DevolucionesController {

    @FXML private TableView<DetallePrestamo> tablaDevoluciones;
    @FXML private TableColumn<DetallePrestamo, Integer> colIdDetalle;

    // Columnas de texto y fechas
    @FXML private TableColumn<DetallePrestamo, String> colUsuario;
    @FXML private TableColumn<DetallePrestamo, String> colLibro;
    @FXML private TableColumn<DetallePrestamo, LocalDate> colFechaPrestamo;
    @FXML private TableColumn<DetallePrestamo, LocalDate> colFechaEsperada;
    @FXML private TableColumn<DetallePrestamo, String> colEstado;

    @FXML private Label lblMensaje;

    private IDetallePrestamoDAO detalleDAO;
    private ILibroDAO libroDAO; // Agregamos el DAO de Libros para el inventario
    private ObservableList<DetallePrestamo> listaActivos;

    @FXML
    public void initialize() {
        // Inicializamos ambos DAOs
        detalleDAO = DAOFactory.getDetallePrestamoDAO();
        libroDAO = DAOFactory.getLibroDAO();

        colIdDetalle.setCellValueFactory(new PropertyValueFactory<>("idDetalle"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colLibro.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        colFechaEsperada.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucionEsperada"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        buscarPrestamosActivos(null);
    }

    @FXML
    private void buscarPrestamosActivos(ActionEvent event) {
        List<DetallePrestamo> activos = detalleDAO.obtenerDetallesActivos();
        if (activos != null) {
            listaActivos = FXCollections.observableArrayList(activos);
            tablaDevoluciones.setItems(listaActivos);
        }
    }

    @FXML
    private void registrarDevolucion(ActionEvent event) {
        DetallePrestamo seleccionado = tablaDevoluciones.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // 1. Actualizamos el estado a 'Devuelto'
            boolean exito = detalleDAO.actualizarEstado(seleccionado.getIdDetalle(), "Devuelto", LocalDate.now());

            if (exito) {
                // 2. RECUPERAMOS EL STOCK DEL LIBRO (El TODO resuelto)
                try {
                    // Buscamos el libro original para saber cuánto stock tiene
                    Libro libroDevuelto = libroDAO.buscarPorId(seleccionado.getIdLibro());
                    if (libroDevuelto != null) {
                        // Le sumamos 1 al inventario y lo actualizamos en la BD
                        libroDevuelto.setCantidadDisponible(libroDevuelto.getCantidadDisponible() + 1);
                        libroDAO.actualizar(libroDevuelto);
                    }
                } catch (Exception e) {
                    System.err.println("La devolución se registró, pero hubo un error al actualizar el stock: " + e.getMessage());
                }

                if (lblMensaje != null) {
                    lblMensaje.setText("Libro devuelto con éxito. Inventario actualizado.");
                    lblMensaje.setStyle("-fx-text-fill: green;");
                    lblMensaje.setVisible(true);
                }

                buscarPrestamosActivos(null); // Refrescamos la tabla
            } else {
                if (lblMensaje != null) {
                    lblMensaje.setText("Error al procesar la devolución.");
                    lblMensaje.setStyle("-fx-text-fill: red;");
                    lblMensaje.setVisible(true);
                }
            }
        } else {
            if (lblMensaje != null) {
                lblMensaje.setText("Seleccione un registro de la tabla.");
                lblMensaje.setStyle("-fx-text-fill: #f39c12;");
                lblMensaje.setVisible(true);
            }
        }
    }
}