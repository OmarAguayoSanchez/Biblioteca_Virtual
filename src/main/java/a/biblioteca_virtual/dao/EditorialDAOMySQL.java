package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Editorial;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@link IEditorialDAO} para el motor de base de datos.
 * Esta clase se encarga de manejar todas las operaciones de persistencia (CRUD)
 * directamente contra la tabla "Editoriales". Utiliza consultas preparadas
 * para garantizar la seguridad contra ataques de inyección SQL.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class EditorialDAOMySQL implements IEditorialDAO {

    /**
     * Inserta un nuevo registro de editorial en la base de datos.
     *
     * @param editorial El objeto {@link Editorial} que contiene los datos (nombre, país, teléfono).
     * @return {@code true} si la inserción fue exitosa y afectó al menos una fila, {@code false} en caso de error SQL.
     */
    @Override
    public boolean insertar(Editorial editorial) {
        String sql = "INSERT INTO Editoriales (nombre, pais, telefono) VALUES (?, ?, ?)";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombre());
            ps.setString(2, editorial.getPais());
            ps.setString(3, editorial.getTelefono());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar editorial: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la información de una editorial existente en la base de datos.
     * Utiliza el ID provisto en el objeto para localizar el registro exacto a modificar.
     *
     * @param editorial El objeto {@link Editorial} con la información actualizada.
     * @return {@code true} si se actualizó correctamente, {@code false} en caso de error.
     */
    @Override
    public boolean actualizar(Editorial editorial) {
        String sql = "UPDATE Editoriales SET nombre=?, pais=?, telefono=? WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombre());
            ps.setString(2, editorial.getPais());
            ps.setString(3, editorial.getTelefono());
            ps.setInt(4, editorial.getIdEditorial());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar editorial: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente el registro de una editorial de la base de datos.
     *
     * @param idEditorial El identificador único de la editorial a eliminar.
     * @return {@code true} si el registro fue eliminado exitosamente, {@code false} si hubo un error
     * (por ejemplo, si la editorial tiene libros asignados y existe una restricción de llave foránea).
     */
    @Override
    public boolean eliminar(int idEditorial) {
        String sql = "DELETE FROM Editoriales WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar editorial: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y recupera una editorial específica utilizando su identificador único.
     *
     * @param idEditorial El identificador único de la editorial buscada.
     * @return El objeto {@link Editorial} poblado con los datos de la base de datos, o {@code null} si no se encontró.
     */
    @Override
    public Editorial buscarPorId(int idEditorial) {
        String sql = "SELECT * FROM Editoriales WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearEditorial(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar editorial: " + e.getMessage());
        }
        return null;
    }

    /**
     * Recupera el listado completo de todas las editoriales registradas en la tabla "Editoriales".
     *
     * @return Una {@link List} de objetos {@link Editorial}. Devuelve una lista vacía si no hay registros.
     */
    @Override
    public List<Editorial> obtenerTodos() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT * FROM Editoriales";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearEditorial(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener editoriales: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método auxiliar privado que convierte una fila del {@link ResultSet} obtenido de MySQL
     * en un objeto de dominio {@link Editorial}.
     * Centraliza la lógica de extracción de columnas para mantener el código limpio.
     *
     * @param rs El {@link ResultSet} posicionado en la fila actual de la iteración.
     * @return Una nueva instancia de {@link Editorial} con sus atributos asignados.
     * @throws SQLException Si ocurre un error al intentar leer el valor de alguna columna.
     */
    private Editorial mapearEditorial(ResultSet rs) throws SQLException {
        Editorial e = new Editorial();
        e.setIdEditorial(rs.getInt("id_editorial"));
        e.setNombre(rs.getString("nombre"));
        e.setPais(rs.getString("pais"));
        e.setTelefono(rs.getString("telefono"));
        return e;
    }
}