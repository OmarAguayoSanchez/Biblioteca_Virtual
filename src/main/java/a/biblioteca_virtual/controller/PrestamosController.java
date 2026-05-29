package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.ILibroDAO;
import a.biblioteca_virtual.dao.IPrestamoDAO;
import a.biblioteca_virtual.dao.IDetallePrestamoDAO;
import a.biblioteca_virtual.dao.IUsuarioDAO;
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.DetallePrestamo;
import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.Model.Prestamo;
import a.biblioteca_virtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de la interfaz gráfica para el módulo de Préstamos.
 * Gestiona la lógica principal de asignación de libros a usuarios, utilizando
 * un sistema de "carrito" temporal. Además, coordina la persistencia transaccional:
 * registra la cabecera del préstamo, guarda cada libro como un detalle individual
 * y actualiza automáticamente el inventario restando las copias prestadas.
 * @author Omar Alejandro Aguayo Sanchez
 * * @version 1.0
 */
public class PrestamosController {

    // --- COMPONENTES DE BÚSQUEDA Y SELECCIÓN ---

    /** Menú desplegable para seleccionar al usuario que solicita el préstamo. */
    @FXML private ComboBox<Usuario> cbUsuario;

    /** Campo de texto para buscar libros ingresando su código ISBN exacto. */
    @FXML private TextField txtBuscarLibro;

    // --- COMPONENTES DEL CARRITO (TABLA) ---

    /** Tabla que funciona como carrito temporal para listar los libros a prestar. */
    @FXML private TableView<Libro> tablaCarrito;

    /** Columna de la tabla del carrito que muestra el ID del libro. */
    @FXML private TableColumn<Libro, Integer> colLibroId;

    /** Columna de la tabla del carrito que muestra el título del libro. */
    @FXML private TableColumn<Libro, String> colLibroTitulo;

    // --- DAOs Y VARIABLES DE ESTADO ---

    /** Lista observable que almacena temporalmente los libros agregados al carrito antes de confirmar. */
    private ObservableList<Libro> carritoLibros;

    private ILibroDAO libroDAO;
    private IPrestamoDAO prestamoDAO;
    private IDetallePrestamoDAO detalleDAO;

    /**
     * Inicializa los componentes de la vista tras cargar el archivo FXML.
     * Configura las fábricas de los DAOs, enlaza las columnas del carrito con el modelo
     * {@link Libro} y carga la lista de usuarios disponibles en el ComboBox.
     */
    @FXML
    public void initialize() {
        libroDAO = DAOFactory.getLibroDAO();
        prestamoDAO = DAOFactory.getPrestamoDAO();
        detalleDAO = DAOFactory.getDetallePrestamoDAO();
        IUsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

        carritoLibros = FXCollections.observableArrayList();

        colLibroId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colLibroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tablaCarrito.setItems(carritoLibros);

        List<Usuario> listaUsuarios = usuarioDAO.obtenerTodos();
        if (listaUsuarios != null) {
            cbUsuario.setItems(FXCollections.observableArrayList(listaUsuarios));
        }
    }

    /**
     * Busca un libro en la base de datos utilizando el ISBN ingresado y, si existe
     * y tiene inventario disponible, lo añade a la lista observable del carrito.
     * Evita que se agregue el mismo libro más de una vez en la misma transacción.
     *
     * @param event El evento disparado al presionar el botón de agregar.
     */
    @FXML
    private void agregarLibroAlCarrito(ActionEvent event) {
        String isbn = txtBuscarLibro.getText().trim();

        if(isbn.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ISBN para buscar.");
            return;
        }

        Libro libro = libroDAO.buscarPorIsbn(isbn);

        if (libro != null && libro.getCantidadDisponible() > 0) {
            if(!carritoLibros.contains(libro)) {
                carritoLibros.add(libro);
                txtBuscarLibro.clear();
            } else {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Aviso", "El libro ya está en el carrito.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Libro no encontrado o sin stock disponible.");
        }
    }

    /**
     * Remueve del carrito el libro que el usuario haya seleccionado en la tabla.
     *
     * @param event El evento disparado al presionar el botón de quitar/eliminar.
     */
    @FXML
    private void quitarLibroDelCarrito(ActionEvent event) {
        Libro seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            carritoLibros.remove(seleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un libro del carrito para quitarlo.");
        }
    }

    /**
     * Procesa y finaliza la transacción completa del préstamo.
     * Sigue un flujo de tres pasos:
     * 1. Inserta la cabecera del préstamo y recupera el ID autogenerado.
     * 2. Itera sobre los libros del carrito para crear los registros de detalle vinculados a ese ID.
     * 3. Descuenta una unidad del stock de cada libro prestado directamente en la base de datos.
     *
     * @param event El evento disparado al confirmar el préstamo.
     */
    @FXML
    private void confirmarPrestamo(ActionEvent event) {
        if (carritoLibros.isEmpty() || cbUsuario.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Selecciona un usuario y agrega al menos un libro al carrito.");
            return;
        }

        // 1. Crear el Préstamo (Cabecera)
        Prestamo nuevoPrestamo = new Prestamo();
        Usuario usuarioSeleccionado = cbUsuario.getValue();
        nuevoPrestamo.setIdUsuario(usuarioSeleccionado.getIdUsuario());
        nuevoPrestamo.setFechaPrestamo(LocalDate.now());

        // Insertamos y obtenemos el ID generado
        int idGenerado = prestamoDAO.insertar(nuevoPrestamo);

        if (idGenerado != -1) {
            // 2. Crear Detalles y RESTAR inventario por cada libro
            LocalDate fechaEntrega = LocalDate.now().plusDays(7);

            for (Libro libro : carritoLibros) {
                // Generar el detalle
                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setIdPrestamo(idGenerado);
                detalle.setIdLibro(libro.getIdLibro());
                detalle.setFechaDevolucionEsperada(fechaEntrega);
                detalle.setEstado("Activo");

                detalleDAO.insertar(detalle);

                // --- LÓGICA DE INVENTARIO: RESTAR 1 ---
                try {
                    int nuevoStock = libro.getCantidadDisponible() - 1;
                    libro.setCantidadDisponible(nuevoStock);
                    libroDAO.actualizar(libro); // Guardamos la nueva cantidad en MySQL
                } catch (Exception e) {
                    System.err.println("Error al actualizar el stock del libro: " + e.getMessage());
                }
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado. El inventario ha sido actualizado.");
            carritoLibros.clear();
            cbUsuario.getSelectionModel().clearSelection();

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al generar la cabecera del préstamo en la base de datos.");
        }
    }

    /**
     * Método de utilidad para la generación de cuadros de diálogo y alertas visuales.
     *
     * @param tipo    La severidad o tipo de mensaje.
     * @param titulo  La cabecera de la ventana.
     * @param mensaje El cuerpo del texto a mostrar al usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}