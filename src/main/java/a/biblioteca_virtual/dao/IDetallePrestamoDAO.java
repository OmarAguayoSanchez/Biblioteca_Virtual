package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.DetallePrestamo;
import java.util.List;

public interface IDetallePrestamoDAO {
    boolean insertar(DetallePrestamo detalle);
    boolean actualizarEstado(int idDetalle, String nuevoEstado, java.time.LocalDate fechaDevolucionReal);
    boolean eliminar(int idDetalle);
    List<DetallePrestamo> buscarPorPrestamo(int idPrestamo);
    List<DetallePrestamo> obtenerDetallesActivos(); // Útil para la vista de Devoluciones
}
