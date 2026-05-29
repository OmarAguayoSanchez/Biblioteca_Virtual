package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Libro;
import java.util.List;

/**
 * Interfaz que define el contrato para el acceso a datos (DAO) de la entidad Libro.
 * Especifica las operaciones de persistencia fundamentales (CRUD) y los métodos
 * de búsqueda especializados necesarios para gestionar el catálogo principal
 * de la biblioteca virtual.
 * * @author Chávez Fabián Karime Jazmin
 * @version 1.0
 */
public interface ILibroDAO {

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro El objeto {@link Libro} que contiene los datos a guardar.
     * @return {@code true} si el libro se registró exitosamente, {@code false} en caso de error.
     */
    boolean insertar(Libro libro);

    /**
     * Actualiza los datos bibliográficos o de inventario de un libro existente.
     * Utiliza el ID del libro proporcionado en el objeto para localizar el registro
     * y sobrescribir sus atributos (incluyendo la cantidad disponible tras un préstamo o devolución).
     *
     * @param libro El objeto {@link Libro} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si ocurrió un error o el registro no existe.
     */
    boolean actualizar(Libro libro);

    /**
     * Elimina permanentemente un libro de la base de datos.
     * Nota: Esta operación puede fallar si el libro tiene historial de préstamos activos
     * o registrados en la tabla de detalles de préstamo, dependiendo de las restricciones
     * de integridad referencial.
     *
     * @param idLibro El identificador único (ID) del libro a eliminar.
     * @return {@code true} si el libro fue eliminado correctamente, {@code false} en caso de error.
     */
    boolean eliminar(int idLibro);

    /**
     * Busca un libro específico utilizando su identificador único interno.
     *
     * @param idLibro El identificador único (PK) del libro.
     * @return Un objeto {@link Libro} con los datos encontrados, o {@code null} si no existe.
     */
    Libro buscarPorId(int idLibro);

    /**
     * Busca un libro específico utilizando su código ISBN (International Standard Book Number).
     * Ideal para el escaneo de códigos de barras o búsquedas exactas al momento de hacer un préstamo.
     *
     * @param isbn El código ISBN exacto del libro a buscar.
     * @return Un objeto {@link Libro} si se encuentra la coincidencia, o {@code null} si no existe.
     */
    Libro buscarPorIsbn(String isbn);

    /**
     * Busca una lista de libros que coincidan total o parcialmente con un título dado.
     * Este método es utilizado para el motor de búsqueda en la interfaz gráfica del catálogo.
     *
     * @param titulo El título o fragmento del título a buscar.
     * @return Una {@link List} de objetos {@link Libro} que coincidan con el criterio de búsqueda.
     */
    List<Libro> buscarPorTitulo(String titulo);

    /**
     * Recupera el catálogo completo de libros registrados en el sistema.
     *
     * @return Una {@link List} con todos los objetos {@link Libro} de la base de datos.
     */
    List<Libro> obtenerTodos();
}
