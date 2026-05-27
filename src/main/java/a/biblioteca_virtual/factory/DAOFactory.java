package a.biblioteca_virtual.factory;

import a.biblioteca_virtual.dao.*;

/**
 * Fábrica centralizada para instanciar los objetos de acceso a datos (DAOs).
 * Aplica el patrón Factory para desacoplar la lógica de negocio (controladores)
 * de la implementación técnica de la base de datos (MySQL).
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class DAOFactory {

    /**
     * Devuelve la implementación concreta para el manejo de Usuarios.
     * @return IUsuarioDAO configurado para la base de datos actual.
     */
    public static IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOMySQL();
    }

    /**
     * Devuelve la implementación concreta para el manejo de Libros.
     * @return ILibroDAO configurado para la base de datos actual.
     */
    public static ILibroDAO getLibroDAO() {
        return new LibroDAOMySQL();
    }

    /**
     * Devuelve la implementación concreta para el manejo de Autores.
     * @return IAutorDAO configurado para la base de datos actual.
     */
    public static IAutorDAO getAutorDAO() {
        return new AutorDAOMySQL();
    }

    /**
     * Devuelve la implementación concreta para el manejo de Editoriales.
     * @return IEditorialDAO configurado para la base de datos actual.
     */
    public static IEditorialDAO getEditorialDAO() {
        return new EditorialDAOMySQL();
    }

    /**
     * Devuelve la implementación concreta para la cabecera de los Préstamos.
     * @return IPrestamoDAO configurado para la base de datos actual.
     */
    public static IPrestamoDAO getPrestamoDAO() {
        return new PrestamoDAOMySQL();
    }

    /**
     * Devuelve la implementación concreta para el detalle de los Préstamos (los libros).
     * @return IDetallePrestamoDAO configurado para la base de datos actual.
     */
    public static IDetallePrestamoDAO getDetallePrestamoDAO() {
        return new DetallePrestamoDAOMySQL();
    }
}
