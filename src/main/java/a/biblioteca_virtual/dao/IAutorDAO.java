package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Autor;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos (DAO) de la entidad Autor.
 * Especifica las operaciones básicas de creación, lectura, actualización y eliminación (CRUD).
 *
 * * @author Chávez Fabián Karime Jazmin
 * @version 1.0
 */
public interface IAutorDAO {

    /**
     * Inserta un nuevo autor en la base de datos.
     *
     * @param autor El objeto {@link Autor} que contiene los datos a guardar.
     * @return {@code true} si el registro se insertó correctamente, {@code false} en caso contrario.
     */
    boolean insertar(Autor autor);

    /**
     * Actualiza los datos de un autor existente en la base de datos.
     * Utiliza el ID del autor proporcionado para encontrar el registro y sobrescribir
     * el resto de sus atributos.
     *
     * @param autor El objeto {@link Autor} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si ocurrió un error o no se encontró el autor.
     */
    boolean actualizar(Autor autor);

    /**
     * Elimina permanentemente un autor de la base de datos utilizando su identificador único.
     * Nota: Puede fallar si el autor está referenciado en otras tablas (ej. libros asignados),
     * dependiendo de las restricciones de llave foránea en la base de datos.
     *
     * @param idAutor El identificador único (ID) del autor a eliminar.
     * @return {@code true} si el registro fue eliminado correctamente, {@code false} en caso de error.
     */
    boolean eliminar(int idAutor);

    /**
     * Busca y recupera un autor específico mediante su identificador único.
     *
     * @param idAutor El identificador único (ID) del autor que se desea buscar.
     * @return Un objeto {@link Autor} con los datos encontrados, o {@code null} si el ID no existe en la base de datos.
     */
    Autor buscarPorId(int idAutor);

    /**
     * Recupera una lista completa con todos los autores registrados en la base de datos.
     *
     * @return Una {@link List} de objetos {@link Autor}. Si no hay registros, devuelve una lista vacía.
     */
    List<Autor> obtenerTodos();
}
