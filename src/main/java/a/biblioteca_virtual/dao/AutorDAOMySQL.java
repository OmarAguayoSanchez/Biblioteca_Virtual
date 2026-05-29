package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Autor;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@link IAutorDAO} para la base de datos.
 * Esta clase maneja todas las operaciones directas (CRUD) hacia la tabla "Autores",
 * utilizando consultas preparadas para prevenir inyecciones SQL y garantizando
 * el mapeo correcto entre los registros relacionales y los objetos.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class AutorDAOMySQL implements IAutorDAO {

    /**
     * Inserta un nuevo registro de autor en la base de datos.
     * Convierte automáticamente la fecha de {@link java.time.LocalDate} a {@link java.sql.Date}
     * para que sea compatible con el motor de base de datos.
     *
     * @param autor El objeto {@link Autor} que contiene los datos a guardar.
     * @return {@code true} si la inserción afectó al menos a una fila, {@code false} en caso de error SQL.
     */
    @Override
    public boolean insertar(Autor autor) {
        String sql = "INSERT INTO Autores (nombre, nacionalidad, fecha_nacimiento) VALUES (?, ?, ?)";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getNacionalidad());
            ps.setDate(3, autor.getFechaNacimiento() != null ? Date.valueOf(autor.getFechaNacimiento()) : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un autor existente en la tabla "Autores".
     *
     * @param autor El objeto {@link Autor} con la información modificada y su ID original.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso de error.
     */
    @Override
    public boolean actualizar(Autor autor) {
        String sql = "UPDATE Autores SET nombre=?, nacionalidad=?, fecha_nacimiento=? WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getNacionalidad());
            ps.setDate(3, autor.getFechaNacimiento() != null ? Date.valueOf(autor.getFechaNacimiento()) : null);
            ps.setInt(4, autor.getIdAutor());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente a un autor utilizando su ID.
     *
     * @param idAutor El identificador único del autor a eliminar.
     * @return {@code true} si se eliminó correctamente, {@code false} en caso de error (ej. restricción de llave foránea).
     */
    @Override
    public boolean eliminar(int idAutor) {
        String sql = "DELETE FROM Autores WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un autor específico por su ID.
     *
     * @param idAutor El identificador único del autor.
     * @return El objeto {@link Autor} encontrado, o {@code null} si no existe en la base de datos.
     */
    @Override
    public Autor buscarPorId(int idAutor) {
        String sql = "SELECT * FROM Autores WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearAutor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar autor: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los autores registrados en la base de datos.
     *
     * @return Una lista ({@link List}) de objetos {@link Autor}. Retorna una lista vacía si no hay registros.
     */
    @Override
    public List<Autor> obtenerTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Autores";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearAutor(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener autores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método auxiliar privado que convierte una fila del {@link ResultSet} en un objeto {@link Autor}.
     * Centraliza la lógica de mapeo para evitar código duplicado en los métodos de búsqueda.
     *
     * @param rs El {@link ResultSet} posicionado en la fila actual.
     * @return Una nueva instancia de {@link Autor} poblada con los datos de la fila.
     * @throws SQLException Si ocurre algún problema al leer las columnas de la base de datos.
     */
    private Autor mapearAutor(ResultSet rs) throws SQLException {
        Autor a = new Autor();
        a.setIdAutor(rs.getInt("id_autor"));
        a.setNombre(rs.getString("nombre"));
        a.setNacionalidad(rs.getString("nacionalidad"));
        if (rs.getDate("fecha_nacimiento") != null) {
            a.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        }
        return a;
    }
}