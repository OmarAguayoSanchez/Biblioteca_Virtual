package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Autor;
import java.util.List;

public interface IAutorDAO {
    boolean insertar(Autor autor);
    boolean actualizar(Autor autor);
    boolean eliminar(int idAutor);
    Autor buscarPorId(int idAutor);
    List<Autor> obtenerTodos();
}
