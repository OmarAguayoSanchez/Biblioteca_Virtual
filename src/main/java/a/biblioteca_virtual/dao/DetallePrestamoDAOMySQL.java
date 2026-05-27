package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.DetallePrestamo;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePrestamoDAOMySQL implements IDetallePrestamoDAO {

    @Override
    public boolean insertar(DetallePrestamo detalle) {
        String sql = "INSERT INTO Detalle_Prestamo (id_prestamo, id_libro, fecha_devolucion_esperada, estado) VALUES (?, ?, ?, ?)";
<<<<<<< HEAD
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
=======
        try (Connection con = ConexionMySQL.getConnection();
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPrestamo());
            ps.setInt(2, detalle.getIdLibro());
            ps.setDate(3, Date.valueOf(detalle.getFechaDevolucionEsperada()));
            ps.setString(4, detalle.getEstado() != null ? detalle.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de préstamo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarEstado(int idDetalle, String nuevoEstado, java.time.LocalDate fechaDevolucionReal) {
        String sql = "UPDATE Detalle_Prestamo SET estado=?, fecha_devolucion_real=? WHERE id_detalle=?";
<<<<<<< HEAD
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
=======
        try (Connection con = ConexionMySQL.getConnection();
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setDate(2, fechaDevolucionReal != null ? Date.valueOf(fechaDevolucionReal) : null);
            ps.setInt(3, idDetalle);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del libro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idDetalle) {
        String sql = "DELETE FROM Detalle_Prestamo WHERE id_detalle=?";
<<<<<<< HEAD
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
=======
        try (Connection con = ConexionMySQL.getConnection();
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DetallePrestamo> buscarPorPrestamo(int idPrestamo) {
        List<DetallePrestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Prestamo WHERE id_prestamo=?";
<<<<<<< HEAD
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
=======
        try (Connection con = ConexionMySQL.getConnection();
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDetalle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar detalles del préstamo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<DetallePrestamo> obtenerDetallesActivos() {
        List<DetallePrestamo> lista = new ArrayList<>();
<<<<<<< HEAD

        // Consulta JOIN con el nombre de columna CORREGIDO (fecha_devolucion_esperada)
        String sql = "SELECT d.id_detalle, d.id_libro, d.fecha_devolucion_esperada, d.estado, " +
                "p.fecha_prestamo, u.username, l.titulo " +
                "FROM Detalle_Prestamo d " +
                "INNER JOIN prestamos p ON d.id_prestamo = p.id_prestamo " +
                "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                "INNER JOIN libros l ON d.id_libro = l.id_libro " +
                "WHERE d.estado = 'Activo'";

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                DetallePrestamo dp = new DetallePrestamo();

                // Datos originales del detalle
                dp.setIdDetalle(rs.getInt("id_detalle"));
                dp.setIdLibro(rs.getInt("id_libro"));

                // LEEMOS LA COLUMNA CON SU NOMBRE REAL
                dp.setFechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada").toLocalDate());
                dp.setEstado(rs.getString("estado"));

                // NUEVOS DATOS TRAÍDOS CON EL JOIN
                dp.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                dp.setNombreUsuario(rs.getString("username"));
                dp.setTituloLibro(rs.getString("titulo"));

                lista.add(dp);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles activos con JOIN: " + e.getMessage());
=======
        String sql = "SELECT * FROM Detalle_Prestamo WHERE estado = 'Activo' OR estado = 'Atrasado'";
        try (Connection con = ConexionMySQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearDetalle(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles activos: " + e.getMessage());
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
        }
        return lista;
    }

    private DetallePrestamo mapearDetalle(ResultSet rs) throws SQLException {
        DetallePrestamo d = new DetallePrestamo();
        d.setIdDetalle(rs.getInt("id_detalle"));
        d.setIdPrestamo(rs.getInt("id_prestamo"));
        d.setIdLibro(rs.getInt("id_libro"));
        d.setFechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada").toLocalDate());

        if (rs.getDate("fecha_devolucion_real") != null) {
            d.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real").toLocalDate());
        }
        d.setEstado(rs.getString("estado"));
        return d;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
