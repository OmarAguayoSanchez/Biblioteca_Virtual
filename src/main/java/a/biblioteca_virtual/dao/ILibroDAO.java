package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Libro;
import java.util.List;

public interface ILibroDAO {
    boolean insertar(Libro libro);
    boolean actualizar(Libro libro);
    boolean eliminar(int idLibro);
    Libro buscarPorId(int idLibro);
    Libro buscarPorIsbn(String isbn);
    List<Libro> buscarPorTitulo(String titulo);
    List<Libro> obtenerTodos();
}
