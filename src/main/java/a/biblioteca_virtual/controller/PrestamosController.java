package a.biblioteca_virtual.controller;

import a.biblioteca_virtual.dao.ILibroDAO;
import a.biblioteca_virtual.dao.IPrestamoDAO;
import a.biblioteca_virtual.dao.IDetallePrestamoDAO;
import a.biblioteca_virtual.dao.IUsuarioDAO; // <-- Importamos el DAO de usuarios
import a.biblioteca_virtual.factory.DAOFactory;
import a.biblioteca_virtual.Model.DetallePrestamo;
import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.Model.Prestamo;
import a.biblioteca_virtual.Model.Usuario; // <-- Importamos el modelo de usuarios
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class PrestamosController {

    // CAMBIO CLAVE: Ahora el ComboBox maneja objetos Usuario completos
    @FXML private ComboBox<Usuario> cbUsuario;
    @FXML private TextField txtBuscarLibro;

    @FXML private TableView<Libro> tablaCarrito;
    @FXML private TableColumn<Libro, Integer> colLibroId;
    @FXML private TableColumn<Libro, String> colLibroTitulo;

    private ObservableList<Libro> carritoLibros;
    private ILibroDAO libroDAO;
    private IPrestamoDAO prestamoDAO;
    private IDetallePrestamoDAO detalleDAO;

    @FXML
    public void initialize() {
        libroDAO = DAOFactory.getLibroDAO();
        prestamoDAO = DAOFactory.getPrestamoDAO();
        detalleDAO = DAOFactory.getDetallePrestamoDAO();

        // Instanciamos el DAO de Usuarios
        IUsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

        carritoLibros = FXCollections.observableArrayList();

        colLibroId.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        colLibroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tablaCarrito.setItems(carritoLibros);

        // Cargamos los usuarios reales desde la Base de Datos
        List<Usuario> listaUsuarios = usuarioDAO.obtenerTodos();
        if (listaUsuarios != null) {
            cbUsuario.setItems(FXCollections.observableArrayList(listaUsuarios));
        }
    }

    @FXML
    private void agregarLibroAlCarrito(ActionEvent event) {
        String isbn = txtBuscarLibro.getText().trim();

        if(isbn.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ISBN para buscar.");
            return;
        }

        Libro libro = libroDAO.buscarPorIsbn(isbn);

        if (libro != null && libro.getCantidadDisponible() > 0) {
            // Evitar agregar el mismo libro dos veces al carrito
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

    @FXML
    private void quitarLibroDelCarrito(ActionEvent event) {
        Libro seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            carritoLibros.remove(seleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un libro del carrito para quitarlo.");
        }
    }

    @FXML
    private void confirmarPrestamo(ActionEvent event) {
        if (carritoLibros.isEmpty() || cbUsuario.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Selecciona un usuario y agrega al menos un libro al carrito.");
            return;
        }

        // 1. Crear el Préstamo (Cabecera)
        Prestamo nuevoPrestamo = new Prestamo();

        // Extraemos el ID del usuario seleccionado
        Usuario usuarioSeleccionado = cbUsuario.getValue();
        nuevoPrestamo.setIdUsuario(usuarioSeleccionado.getIdUsuario());
        nuevoPrestamo.setFechaPrestamo(LocalDate.now());

        // El DAO nos devuelve el ID autogenerado
        int idGenerado = prestamoDAO.insertar(nuevoPrestamo);

        if (idGenerado != -1) {
            // 2. Crear los Detalles por cada libro en el carrito
            LocalDate fechaEntrega = LocalDate.now().plusDays(7); // Damos 7 días de préstamo

            for (Libro libro : carritoLibros) {
                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setIdPrestamo(idGenerado);
                detalle.setIdLibro(libro.getIdLibro());
                detalle.setFechaDevolucionEsperada(fechaEntrega);
                detalle.setEstado("Activo");

                detalleDAO.insertar(detalle);

                // TODO: Aquí podrías restar 1 a la cantidadDisponible del libro usando libroDAO.actualizar()
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado exitosamente.");
            carritoLibros.clear();
            cbUsuario.getSelectionModel().clearSelection();

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al generar la cabecera del préstamo en la base de datos.");
        }
    }

    // Método auxiliar para mostrar ventanas emergentes (Alertas)
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}