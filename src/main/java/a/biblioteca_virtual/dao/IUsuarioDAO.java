package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.Model.Usuario;

import java.util.List;

/**
 * Interfaz que define el Login para la clase Usuario.
 * Establece las operaciones necesarias para el control de acceso (login)
 * y la creación de nuevas cuentas en el sistema.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public interface IUsuarioDAO {

    /**
     * Verifica las credenciales de un usuario para permitirle el acceso al sistema.
     * La implementación de este método debe encargarse de aplicar el algoritmo de hash
     * a la contraseña plana para compararla con la almacenada de forma segura en la base de datos.
     *
     * @param username      El nombre de usuario que intenta acceder.
     * @param passwordPlana La contraseña original escrita por el usuario, sin ningún tipo de cifrado.
     * @return Un String con el rol del usuario si las credenciales son válidas.
     *         Retorna null si el usuario no existe o la contraseña es incorrecta.
     */
    String autenticar(String username, String passwordPlana);

    /**
     * Registra un nuevo usuario en el sistema.
     * La implementación debe asegurar que la contraseña plana sea cifrada con hash
     * antes de ser persistida en la base de datos.
     *
     * @param nombreCompleto El nombre real del usuario.
     * @param correo         El correo electrónico del usuario.
     * @param username       El nombre de usuario con el que iniciará sesión.
     * @param passwordPlana  La contraseña elegida por el usuario, sin cifrar.
     * @param rol            El nivel de acceso asignado al nuevo usuario.
     * @return true si el usuario se registró exitosamente, false si ocurrió un error
     */
    boolean registrar(String nombreCompleto, String correo, String username, String passwordPlana, String rol);
    List<Usuario> obtenerTodos();
    boolean eliminar(int idUsuario);
}
