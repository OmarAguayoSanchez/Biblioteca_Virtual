package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Prestamo;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOMySQL implements IPrestamoDAO {

    @Override
    public int insertar(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamos (id_usuario, fecha_prestamo) VALUES (?, ?)";
        int idGenerado = -1;

        // El segundo parámetro indica que queremos recuperar la Primary Key autoincrementable
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, prestamo.getIdUsuario());
            ps.setDate(2, Date.valueOf(prestamo.getFechaPrestamo()));

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1); // Recuperamos el ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al generar préstamo: " + e.getMessage());
        }
        return idGenerado;
    }

    @Override
    public boolean eliminar(int idPrestamo) {
        String sql = "DELETE FROM Prestamos WHERE id_prestamo=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Prestamo buscarPorId(int idPrestamo) {
        String sql = "SELECT * FROM Prestamos WHERE id_prestamo=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPrestamo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar préstamo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Prestamo> buscarPorUsuario(int idUsuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE id_usuario=?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar préstamos del usuario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Prestamo> obtenerTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos";
        try (Connection con = ConexionMySQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearPrestamo(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos: " + e.getMessage());
        }
        return lista;
    }

    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setIdPrestamo(rs.getInt("id_prestamo"));
        p.setIdUsuario(rs.getInt("id_usuario"));
        p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        return p;
    }
}
