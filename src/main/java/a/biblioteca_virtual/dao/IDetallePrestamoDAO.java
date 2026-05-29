package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.DetallePrestamo;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos (DAO) de los detalles de préstamo.
 * Gestiona las operaciones de persistencia para los libros individuales que forman parte
 * de una transacción de préstamo principal.
 * * @author Chávez Fabián Karime Jazmin
 * @version 1.0
 */
public interface IDetallePrestamoDAO {

    /**
     * Inserta un nuevo detalle de préstamo en la base de datos.
     * Este método asocia un libro específico a un préstamo (cabecera) y establece
     * su fecha esperada de devolución.
     *
     * @param detalle El objeto {@link DetallePrestamo} con los datos a registrar.
     * @return {@code true} si el registro se insertó correctamente, {@code false} en caso contrario.
     */
    boolean insertar(DetallePrestamo detalle);

    /**
     * Actualiza el estado actual de un libro prestado y registra la fecha en que fue entregado.
     * Es utilizado principalmente durante el proceso de devolución de libros en el sistema.
     *
     * @param idDetalle           El identificador único del detalle de préstamo a actualizar.
     * @param nuevoEstado         El nuevo estado a asignar (ej. "Devuelto", "Atrasado").
     * @param fechaDevolucionReal La fecha exacta en la que el usuario entregó el libro físico.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso de error.
     */
    boolean actualizarEstado(int idDetalle, String nuevoEstado, java.time.LocalDate fechaDevolucionReal);

    /**
     * Elimina permanentemente un detalle de préstamo de la base de datos.
     *
     * @param idDetalle El identificador único del registro a eliminar.
     * @return {@code true} si el detalle fue eliminado correctamente, {@code false} en caso de error.
     */
    boolean eliminar(int idDetalle);

    /**
     * Busca y recupera todos los detalles (libros) que pertenecen a una misma transacción de préstamo.
     * Ideal para mostrar un "ticket" o resumen histórico de un préstamo específico.
     *
     * @param idPrestamo El identificador de la cabecera del préstamo (Llave Foránea).
     * @return Una {@link List} de objetos {@link DetallePrestamo} asociados a ese préstamo.
     */
    List<DetallePrestamo> buscarPorPrestamo(int idPrestamo);

    /**
     * Recupera todos los detalles de préstamo cuyo estado indique que el libro aún no ha sido devuelto
     * Este método utiliza un cruce de tablas para traer los nombres reales del usuario y del libro,
     * alimentando directamente la vista de Devoluciones.
     *
     * @return Una {@link List} de objetos {@link DetallePrestamo} con los préstamos vigentes.
     */
    List<DetallePrestamo> obtenerDetallesActivos();
}