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

/**
 * Controlador de la interfaz gráfica para el módulo de Devoluciones.
 * Esta clase gestiona la vista donde los usuarios pueden registrar la entrega
 * de los libros prestados. Se encarga de actualizar el estado del préstamo
 * a "Devuelto" y de incrementar automáticamente el stock del libro en el inventario.
 * * @author Omar Alejandro Aguayo Sanchez
 * @version 1.0
 */
public class DevolucionesController {

    // --- COMPONENTES DE LA TABLA ---

    /** Tabla principal que muestra los libros actualmente prestados (activos). */
    @FXML private TableView<DetallePrestamo> tablaDevoluciones;

    @FXML private TableColumn<DetallePrestamo, Integer> colIdDetalle;

    // Columnas de texto y fechas generadas a partir de consultas JOIN
    @FXML private TableColumn<DetallePrestamo, String> colUsuario;
    @FXML private TableColumn<DetallePrestamo, String> colLibro;
    @FXML private TableColumn<DetallePrestamo, LocalDate> colFechaPrestamo;
    @FXML private TableColumn<DetallePrestamo, LocalDate> colFechaEsperada;
    @FXML private TableColumn<DetallePrestamo, String> colEstado;

    /** Etiqueta para mostrar mensajes de retroalimentación al usuario. */
    @FXML private Label lblMensaje;

    // --- DAOs Y LISTAS ---

    /** DAO para gestionar los detalles de los préstamos en la base de datos. */
    private IDetallePrestamoDAO detalleDAO;

    /** DAO para gestionar el inventario de los libros en la base de datos. */
    private ILibroDAO libroDAO;

    /** Lista observable para mantener sincronizada la tabla visual con los datos obtenidos. */
    private ObservableList<DetallePrestamo> listaActivos;

    /**
     * Método inicializador de JavaFX. Se ejecuta automáticamente al cargar la vista FXML.
     * Configura las dependencias DAO a través del Factory, enlaza las columnas de la tabla
     * con los atributos del modelo {@link DetallePrestamo} y realiza la carga inicial de datos.
     */
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

    /**
     * Consulta la base de datos para obtener todos los detalles de préstamos
     * cuyo estado actual sea "Activo" y los carga en la tabla de la interfaz.
     *
     * @param event El evento disparado por la interfaz (puede ser null si se llama directamente).
     */
    @FXML
    private void buscarPrestamosActivos(ActionEvent event) {
        List<DetallePrestamo> activos = detalleDAO.obtenerDetallesActivos();
        if (activos != null) {
            listaActivos = FXCollections.observableArrayList(activos);
            tablaDevoluciones.setItems(listaActivos);
        }
    }

    /**
     * Procesa la devolución de un libro seleccionado en la tabla.
     * Realiza una transacción de dos pasos:
     * 1. Cambia el estado del detalle de préstamo a "Devuelto" registrando la fecha actual.
     * 2. Recupera el libro de la base de datos y le suma 1 a su cantidad disponible.
     *
     * @param event El evento disparado al presionar el botón de registrar devolución.
     */
    @FXML
    private void registrarDevolucion(ActionEvent event) {
        DetallePrestamo seleccionado = tablaDevoluciones.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // 1. Actualizamos el estado a 'Devuelto' en el historial
            boolean exito = detalleDAO.actualizarEstado(seleccionado.getIdDetalle(), "Devuelto", LocalDate.now());

            if (exito) {
                // 2. RECUPERAMOS EL STOCK DEL LIBRO PARA ACTUALIZAR EL INVENTARIO
                try {
                    // Buscamos el libro original para saber cuánto stock tiene actualmente
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

                buscarPrestamosActivos(null); // Refrescamos la tabla para que el libro desaparezca
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