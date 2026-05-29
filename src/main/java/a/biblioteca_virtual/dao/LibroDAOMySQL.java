package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@link ILibroDAO} para el motor de base de datos MySQL.
 * Esta clase centraliza todas las operaciones de persistencia (CRUD) y consultas de búsqueda
 * relacionadas con la tabla "Libros", manejando llaves foráneas y asegurando
 * la integridad de los datos mediante consultas preparadas.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class LibroDAOMySQL implements ILibroDAO {

    /**
     * Inserta un nuevo registro de libro en la base de datos.
     * Este método mapea todos los campos principales, así como las llaves foráneas
     * hacia las tablas de Autores y Editoriales.
     *
     * @param libro El objeto {@link Libro} que contiene los datos bibliográficos a registrar.
     * @return {@code true} si la inserción fue exitosa y afectó a la base de datos, {@code false} en caso de error SQL.
     */
    @Override
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO Libros (titulo, isbn, anio_publicacion, genero, cantidad_disponible, id_editorial, id_autor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getGenero());
            ps.setInt(5, libro.getCantidadDisponible());
            ps.setInt(6, libro.getIdEditorial());
            ps.setInt(7, libro.getIdAutor());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la información general o el inventario de un libro existente.
     * Es utilizado tanto para modificar metadatos bibliográficos desde el catálogo,
     * como para actualizar la cantidad disponible tras procesar un préstamo o una devolución.
     *
     * @param libro El objeto {@link Libro} con los datos modificados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si hubo un error.
     */
    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE Libros SET titulo=?, isbn=?, anio_publicacion=?, genero=?, cantidad_disponible=?, id_editorial=?, id_autor=? WHERE id_libro=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getGenero());
            ps.setInt(5, libro.getCantidadDisponible());
            ps.setInt(6, libro.getIdEditorial());
            ps.setInt(7, libro.getIdAutor());
            ps.setInt(8, libro.getIdLibro());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina el registro de un libro de manera permanente.
     * Nota: Puede fallar por restricciones de llave foránea si el libro se encuentra asociado
     * a detalles de préstamos históricos.
     *
     * @param idLibro El identificador único del libro a eliminar.
     * @return {@code true} si se eliminó con éxito, {@code false} en caso de error o restricción.
     */
    @Override
    public boolean eliminar(int idLibro) {
        String sql = "DELETE FROM Libros WHERE id_libro=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un libro específico utilizando su código ISBN único.
     * Es ideal para flujos rápidos como la captura por código de barras al hacer un préstamo.
     *
     * @param isbn El código ISBN exacto del libro a buscar.
     * @return Un objeto {@link Libro} si se encuentra la coincidencia exacta, o {@code null} si no existe.
     */
    @Override
    public Libro buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM Libros WHERE isbn=?";
        Libro libro = null;
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = mapearLibro(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ISBN: " + e.getMessage());
        }
        return libro;
    }

    /**
     * Recupera el catálogo completo de todos los libros almacenados en la base de datos.
     *
     * @return Una {@link List} de objetos {@link Libro}. Devuelve una lista vacía si la tabla no tiene registros.
     */
    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libros";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los libros: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un libro específico utilizando su identificador único (ID interno de la tabla).
     *
     * @param idLibro El identificador único (Primary Key) del libro.
     * @return El objeto {@link Libro} correspondiente, o {@code null} si el ID no es válido.
     */
    @Override
    public Libro buscarPorId(int idLibro) {
        String sql = "SELECT * FROM Libros WHERE id_libro=?";
        Libro libro = null;
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = mapearLibro(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
        }
        return libro;
    }

    /**
     * Busca libros en el catálogo cuyo título coincida parcial o totalmente con el término ingresado.
     * Utiliza el operador SQL {@code LIKE} para permitir búsquedas flexibles desde la interfaz de usuario.
     *
     * @param titulo El término, palabra o fragmento de título a buscar.
     * @return Una {@link List} de objetos {@link Libro} que contienen el término de búsqueda.
     * Si no hay coincidencias, devuelve una lista vacía.
     */
    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE titulo LIKE ?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%"); // Búsqueda parcial por contenido
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearLibro(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por título: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método auxiliar privado para centralizar la lógica de mapeo del {@link ResultSet} a un objeto {@link Libro}.
     * Extrae todos los atributos y llaves foráneas de una fila resultante de MySQL.
     *
     * @param rs El {@link ResultSet} posicionado en la fila actual de la iteración.
     * @return Una nueva instancia de {@link Libro} con sus atributos asignados.
     * @throws SQLException Si ocurre un error al intentar acceder a los datos de la columna.
     */
    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setIdLibro(rs.getInt("id_libro"));
        l.setTitulo(rs.getString("titulo"));
        l.setIsbn(rs.getString("isbn"));
        l.setAnioPublicacion(rs.getInt("anio_publicacion"));
        l.setGenero(rs.getString("genero"));
        l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        l.setIdEditorial(rs.getInt("id_editorial"));
        l.setIdAutor(rs.getInt("id_autor"));
        return l;
    }
}