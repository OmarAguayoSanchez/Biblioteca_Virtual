package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Editorial;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAOMySQL implements IEditorialDAO {

    @Override
    public boolean insertar(Editorial editorial) {
        String sql = "INSERT INTO Editoriales (nombre, pais, telefono) VALUES (?, ?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean actualizar(Editorial editorial) {
        String sql = "UPDATE Editoriales SET nombre=?, pais=?, telefono=? WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean eliminar(int idEditorial) {
        String sql = "DELETE FROM Editoriales WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar editorial: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Editorial buscarPorId(int idEditorial) {
        String sql = "SELECT * FROM Editoriales WHERE id_editorial=?";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public List<Editorial> obtenerTodos() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT * FROM Editoriales";
        try (Connection con = ConexionMySQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearEditorial(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener editoriales: " + e.getMessage());
        }
        return lista;
    }

    private Editorial mapearEditorial(ResultSet rs) throws SQLException {
        Editorial e = new Editorial();
        e.setIdEditorial(rs.getInt("id_editorial"));
        e.setNombre(rs.getString("nombre"));
        e.setPais(rs.getString("pais"));
        e.setTelefono(rs.getString("telefono"));
        return e;
    }
}
