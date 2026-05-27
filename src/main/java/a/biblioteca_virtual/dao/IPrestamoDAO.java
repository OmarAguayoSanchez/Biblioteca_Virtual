package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Prestamo;
import java.util.List;

public interface IPrestamoDAO {
    // Devuelve el ID generado del préstamo, o -1 si falla
    int insertar(Prestamo prestamo);
    boolean eliminar(int idPrestamo);
    Prestamo buscarPorId(int idPrestamo);
    List<Prestamo> buscarPorUsuario(int idUsuario);
    List<Prestamo> obtenerTodos();
}
