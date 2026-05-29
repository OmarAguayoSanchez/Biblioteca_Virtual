package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Prestamo;
import a.biblioteca_virtual.database.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@link IPrestamoDAO} para el motor de base de datos.
 * Esta clase maneja las operaciones de persistencia de la "cabecera" de las transacciones
 * de préstamo, vinculando al usuario con la fecha del movimiento.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class PrestamoDAOMySQL implements IPrestamoDAO {

    /**
     * Inserta un nuevo registro de préstamo en la base de datos y recupera el ID generado.
     * Utiliza {@code Statement.RETURN_GENERATED_KEYS} para obtener la Primary Key autoincrementable
     * creada, la cual es estrictamente necesaria para registrar los libros prestados
     * (detalles) en la tabla correspondiente.
     *
     * @param prestamo El objeto {@link Prestamo} que contiene el ID del usuario y la fecha del préstamo.
     * @return El ID (Primary Key) generado para este nuevo préstamo, o {@code -1} si la inserción falló.
     */
    @Override
    public int insertar(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamos (id_usuario, fecha_prestamo) VALUES (?, ?)";
        int idGenerado = -1;

        // El segundo parámetro indica que queremos recuperar la Primary Key autoincrementable
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Elimina permanentemente un registro de préstamo (cabecera) de la base de datos.
     *
     * @param idPrestamo El identificador único del préstamo a eliminar.
     * @return {@code true} si el registro fue eliminado correctamente, {@code false} si ocurrió un error
     */
    @Override
    public boolean eliminar(int idPrestamo) {
        String sql = "DELETE FROM Prestamos WHERE id_prestamo=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y recupera un registro de préstamo específico utilizando su identificador único.
     *
     * @param idPrestamo El identificador único (ID) del préstamo a buscar.
     * @return El objeto {@link Prestamo} con los datos encontrados, o {@code null} si no existe en la base de datos.
     */
    @Override
    public Prestamo buscarPorId(int idPrestamo) {
        String sql = "SELECT * FROM Prestamos WHERE id_prestamo=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Busca y recupera todo el historial de préstamos realizados por un usuario específico.
     *
     * @param idUsuario El identificador único del usuario (Llave Foránea).
     * @return Una {@link List} de objetos {@link Prestamo} asociados al usuario. Devuelve una lista vacía si no tiene historial.
     */
    @Override
    public List<Prestamo> buscarPorUsuario(int idUsuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE id_usuario=?";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
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

    /**
     * Recupera el registro completo de todos los préstamos almacenados en el sistema.
     *
     * @return Una {@link List} de objetos {@link Prestamo}. Devuelve una lista vacía si no hay registros.
     */
    @Override
    public List<Prestamo> obtenerTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos";
        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearPrestamo(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método auxiliar privado que transforma un registro de la tabla "Prestamos" obtenido
     * en un objeto de dominio {@link Prestamo}.
     * Centraliza la lógica de extracción y conversión de tipos (como Date a LocalDate).
     *
     * @param rs El {@link ResultSet} posicionado en la fila actual de la iteración.
     * @return Una nueva instancia de {@link Prestamo} poblada con los datos de la fila.
     * @throws SQLException Si ocurre un error de lectura en las columnas de la base de datos.
     */
    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setIdPrestamo(rs.getInt("id_prestamo"));
        p.setIdUsuario(rs.getInt("id_usuario"));
        p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        return p;
    }
}