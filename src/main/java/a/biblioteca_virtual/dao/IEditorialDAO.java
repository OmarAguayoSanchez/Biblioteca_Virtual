package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Editorial;
import java.util.List;

public interface IEditorialDAO {
    boolean insertar(Editorial editorial);
    boolean actualizar(Editorial editorial);
    boolean eliminar(int idEditorial);
    Editorial buscarPorId(int idEditorial);
    List<Editorial> obtenerTodos();
}