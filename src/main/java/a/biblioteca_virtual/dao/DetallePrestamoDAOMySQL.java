package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.DetallePrestamo;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@link IDetallePrestamoDAO} para el motor de base de datos.
 * Esta clase maneja las operaciones (CRUD) para los registros individuales de libros
 * dentro de un préstamo. Además, resuelve las relaciones complejas entre tablas para
 * alimentar directamente las vistas.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class DetallePrestamoDAOMySQL implements IDetallePrestamoDAO {

    /**
     * Inserta un nuevo detalle de préstamo en la base de datos.
     * Convierte la fecha esperada de devolución de {@link java.time.LocalDate} a {@link java.sql.Date}
     * para su correcta inserción y asigna un estado por defecto si no se proporciona uno.
     *
     * @param detalle El objeto {@link DetallePrestamo} que contiene el ID del préstamo, el ID del libro y la fecha límite.
     * @return {@code true} si la inserción fue exitosa, {@code false} si ocurrió un error SQL.
     */
    @Override
    public boolean insertar(DetallePrestamo detalle) {
        String sql = "INSERT INTO Detalle_Prestamo (id_prestamo, id_libro, fecha_devolucion_esperada, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Actualiza el estado y la fecha de entrega real de un libro prestado.
     * Es invocado principalmente al procesar una devolución desde la interfaz gráfica.
     *
     * @param idDetalle           El identificador único del registro a actualizar.
     * @param nuevoEstado         El nuevo estado a asignar (ej. "Devuelto").
     * @param fechaDevolucionReal La fecha exacta en la que se registra la devolución en el sistema.
     * @return {@code true} si la actualización modificó el registro, {@code false} en caso de error.
     */
    @Override
    public boolean actualizarEstado(int idDetalle, String nuevoEstado, java.time.LocalDate fechaDevolucionReal) {
        String sql = "UPDATE Detalle_Prestamo SET estado=?, fecha_devolucion_real=? WHERE id_detalle=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Elimina permanentemente un registro individual de la tabla de detalles de préstamo.
     *
     * @param idDetalle El identificador único del registro a eliminar.
     * @return {@code true} si el registro fue borrado exitosamente, {@code false} en caso de error.
     */
    @Override
    public boolean eliminar(int idDetalle) {
        String sql = "DELETE FROM Detalle_Prestamo WHERE id_detalle=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera la lista de libros (detalles) asociados a la cabecera de un préstamo específico.
     * Utiliza un método auxiliar para mapear los resultados básicos de la tabla.
     *
     * @param idPrestamo El identificador del préstamo principal.
     * @return Una lista de objetos {@link DetallePrestamo} pertenecientes a esa transacción.
     */
    @Override
    public List<DetallePrestamo> buscarPorPrestamo(int idPrestamo) {
        List<DetallePrestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Prestamo WHERE id_prestamo=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Recupera todos los libros que se encuentran prestados actualmente (estado 'Activo').
     * Ejecuta una consulta INNER JOIN compleja que cruza las tablas de Detalles, Préstamos,
     * Usuarios y Libros para consolidar la información de lectura humana que requiere la tabla
     * de la pantalla de devoluciones.
     *
     * @return Una lista de objetos {@link DetallePrestamo} enriquecidos con datos virtuales (nombres y títulos).
     */
    @Override
    public List<DetallePrestamo> obtenerDetallesActivos() {
        List<DetallePrestamo> lista = new ArrayList<>();

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
        }
        return lista;
    }

    /**
     * Método auxiliar privado que transforma un registro simple de la tabla "Detalle_Prestamo"
     * en un objeto Java. Diseñado para centralizar el mapeo de columnas básicas y evitar redundancia.
     *
     * @param rs El {@link ResultSet} apuntando a la fila actual extraída de la base de datos.
     * @return Una nueva instancia de {@link DetallePrestamo} con sus atributos poblados.
     * @throws SQLException Si ocurre algún error de lectura en las columnas de la tabla.
     */
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
}