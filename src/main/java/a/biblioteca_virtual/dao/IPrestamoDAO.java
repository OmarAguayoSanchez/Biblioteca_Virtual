package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Prestamo;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos (DAO) de la entidad Prestamo.
 * Gestiona las operaciones de persistencia correspondientes a la cabecera de las
 * transacciones, vinculando a los usuarios con la fecha de su solicitud.
 * * @author Chávez Fabián Karime Jazmin
 * @version 1.0
 */
public interface IPrestamoDAO {

    /**
     * Inserta un nuevo registro de préstamo en la base de datos.
     * A diferencia de otras operaciones de inserción, este método recupera y devuelve
     * el ID autogenerado por el motor de base de datos, el cual es estrictamente necesario
     * para vincular los detalles del préstamo a esta transacción.
     *
     * @param prestamo El objeto {@link Prestamo} que contiene la información del usuario y la fecha.
     * @return El ID generado para el nuevo préstamo, o {@code -1} si la inserción falla.
     */
    int insertar(Prestamo prestamo);

    /**
     * Elimina permanentemente un registro de préstamo de la base de datos.
     *
     * @param idPrestamo El identificador único (ID) del préstamo a eliminar.
     * @return {@code true} si el registro fue eliminado exitosamente, {@code false} en caso de error.
     */
    boolean eliminar(int idPrestamo);

    /**
     * Busca y recupera la cabecera de un préstamo específico mediante su identificador único.
     *
     * @param idPrestamo El identificador único (PK) del préstamo que se desea buscar.
     * @return Un objeto {@link Prestamo} con los datos encontrados, o {@code null} si no existe.
     */
    Prestamo buscarPorId(int idPrestamo);

    /**
     * Busca y recupera el historial completo de préstamos realizados por un usuario en particular.
     * Ideal para mostrar el perfil del usuario o sus transacciones pasadas en la interfaz gráfica.
     *
     * @param idUsuario El identificador único del usuario (Llave Foránea).
     * @return Una {@link List} de objetos {@link Prestamo} asociados a ese usuario.
     */
    List<Prestamo> buscarPorUsuario(int idUsuario);

    /**
     * Recupera el registro completo de todas las cabeceras de préstamo en el sistema.
     * Útil para generar reportes generales o auditorías de movimientos históricos.
     *
     * @return Una {@link List} con todos los objetos {@link Prestamo} registrados.
     */
    List<Prestamo> obtenerTodos();
}
