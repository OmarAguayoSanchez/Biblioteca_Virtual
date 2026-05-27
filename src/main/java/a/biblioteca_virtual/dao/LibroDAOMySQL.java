package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Libro;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOMySQL implements ILibroDAO {

    @Override
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO Libros (titulo, isbn, anio_publicacion, genero, cantidad_disponible, id_editorial, id_autor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE Libros SET titulo=?, isbn=?, anio_publicacion=?, genero=?, cantidad_disponible=?, id_editorial=?, id_autor=? WHERE id_libro=?";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean eliminar(int idLibro) {
        String sql = "DELETE FROM Libros WHERE id_libro=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libros";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public Libro buscarPorId(int idLibro) {
        String sql = "SELECT * FROM Libros WHERE id_libro=?";
        Libro libro = null;
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE titulo LIKE ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%"); // Búsqueda parcial
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

    // Método auxiliar privado para centralizar el mapeo del ResultSet al Objeto Libro
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
