package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Autor;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAOMySQL implements IAutorDAO {

    @Override
    public boolean insertar(Autor autor) {
        String sql = "INSERT INTO Autores (nombre, nacionalidad, fecha_nacimiento) VALUES (?, ?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean actualizar(Autor autor) {
        String sql = "UPDATE Autores SET nombre=?, nacionalidad=?, fecha_nacimiento=? WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public boolean eliminar(int idAutor) {
        String sql = "DELETE FROM Autores WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar autor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Autor buscarPorId(int idAutor) {
        String sql = "SELECT * FROM Autores WHERE id_autor=?";
        try (Connection con = ConexionMySQL.getConnection();
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

    @Override
    public List<Autor> obtenerTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Autores";
        try (Connection con = ConexionMySQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearAutor(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener autores: " + e.getMessage());
        }
        return lista;
    }

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
