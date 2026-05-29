package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Editorial;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos (DAO) de la entidad Editorial.
 * Especifica las operaciones básicas de creación, lectura, actualización y eliminación (CRUD)
 * necesarias para gestionar los catálogos de casas editoriales en cualquier mecanismo
 * de persistencia subyacente.
 * * @author Chávez Fabián Karime Jazmin
 * @version 1.0
 */
public interface IEditorialDAO {

    /**
     * Inserta una nueva editorial en la base de datos.
     *
     * @param editorial El objeto {@link Editorial} que contiene los datos a guardar (nombre, país, teléfono).
     * @return {@code true} si el registro se insertó correctamente, {@code false} en caso de error.
     */
    boolean insertar(Editorial editorial);

    /**
     * Actualiza los datos de una editorial existente en la base de datos.
     * Utiliza el ID de la editorial proporcionado dentro del objeto para localizar
     * el registro y sobrescribir el resto de sus atributos.
     *
     * @param editorial El objeto {@link Editorial} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si ocurrió un error o el registro no existe.
     */
    boolean actualizar(Editorial editorial);

    /**
     * Elimina permanentemente una editorial de la base de datos utilizando su identificador único.
     * Nota: La eliminación puede fallar si la editorial tiene libros asociados activos,
     * dependiendo de las restricciones de llave foránea configuradas en la base de datos.
     *
     * @param idEditorial El identificador único (ID) de la editorial a eliminar.
     * @return {@code true} si el registro fue eliminado correctamente, {@code false} en caso de error.
     */
    boolean eliminar(int idEditorial);

    /**
     * Busca y recupera los datos de una editorial específica mediante su identificador único.
     *
     * @param idEditorial El identificador único (ID) de la editorial que se desea buscar.
     * @return Un objeto {@link Editorial} con los datos encontrados, o {@code null} si el ID no existe en la base de datos.
     */
    Editorial buscarPorId(int idEditorial);

    /**
     * Recupera una lista completa con todas las editoriales registradas en el sistema.
     * Ideal para llenar menús desplegables (ComboBox) en la interfaz gráfica.
     *
     * @return Una {@link List} de objetos {@link Editorial}. Si no hay registros, devuelve una lista vacía.
     */
    List<Editorial> obtenerTodos();
}