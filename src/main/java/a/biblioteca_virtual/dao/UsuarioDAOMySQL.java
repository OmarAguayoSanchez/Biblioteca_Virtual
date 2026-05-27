package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.database.ConexionMySQL;
import a.biblioteca_virtual.Model.Usuario;
import a.biblioteca_virtual.seguridad.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Usuario usando MySQL.
 * Gestiona la autenticación, el registro, la consulta y la eliminación
 * de usuarios en el sistema de manera segura.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class UsuarioDAOMySQL implements IUsuarioDAO {

    // --- CONSTANTES SQL ---
    private static final String SQL_SELECT_ROLE =
            "SELECT rol FROM usuarios WHERE username = ? AND password_hash = ?";

    private static final String SQL_INSERT_USER =
            "INSERT INTO usuarios (nombre_completo, correo, username, password_hash, rol) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
            "SELECT id_usuario, nombre_completo, correo, username, rol FROM usuarios";

    private static final String SQL_DELETE =
            "DELETE FROM usuarios WHERE id_usuario = ?";

    // --- CONSTANTES DE ERRORES ---
    /** Código de error de MySQL devuelto cuando se viola una restricción UNIQUE */
    private static final int MYSQL_ERR_DUPLICATE_ENTRY = 1062;

    /**
     * Instancia de la clase de conexión para interactuar con la base de datos.
     */
    private final ConexionMySQL conexionMySQL;

    /**
     * Constructor que inicializa el acceso al Singleton de conexión.
     */
    public UsuarioDAOMySQL() {
        this.conexionMySQL = ConexionMySQL.getInstancia();
    }

    /**
     * Verifica las credenciales de un usuario.
     * Convierte la contraseña plana utilizando el método generarSHA256 de HashUtil
     * antes de consultar la base de datos.
     *
     * @param username      El nombre de usuario ingresado.
     * @param passwordPlana La contraseña original escrita en el formulario.
     * @return El rol del usuario si es válido, o null si las credenciales fallan.
     */
    @Override
    public String autenticar(String username, String passwordPlana) {
        // Vinculación corregida con el nombre exacto de tu método en HashUtil
        String hashCalculado = HashUtil.generarSHA256(passwordPlana);
        Connection con = conexionMySQL.getConexion();

        try (PreparedStatement ps = con.prepareStatement(SQL_SELECT_ROLE)) {
            ps.setString(1, username);
            ps.setString(2, hashCalculado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rol");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }

        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos, asegurando el cifrado de su contraseña.
     *
     * @param nombreCompleto El nombre real del usuario.
     * @param correo         El correo electrónico.
     * @param username       El nombre de usuario para el login.
     * @param passwordPlana  La contraseña elegida por el usuario en texto plano.
     * @param rol            El nivel de permisos asignado ("ADMIN" o "CLIENT").
     * @return true si se registró exitosamente, false si ocurrió un error.
     */
    @Override
    public boolean registrar(String nombreCompleto, String correo, String username, String passwordPlana, String rol) {
        // Vinculación corregida con el nombre exacto de tu método en HashUtil
        String hashParaGuardar = HashUtil.generarSHA256(passwordPlana);
        Connection con = conexionMySQL.getConexion();

        try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_USER)) {
            ps.setString(1, nombreCompleto);
            ps.setString(2, correo);
            ps.setString(3, username);
            ps.setString(4, hashParaGuardar);
            ps.setString(5, rol);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_ERR_DUPLICATE_ENTRY) {
                System.out.println("Aviso: El nombre de usuario o el correo ya están registrados.");
            } else {
                System.err.println("Error al registrar usuario: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados en el sistema.
     *
     * @return Lista de objetos Usuario recuperados de la base de datos.
     */
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        Connection con = conexionMySQL.getConexion();

        try (PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setCorreo(rs.getString("correo"));
                u.setUsername(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina un usuario del sistema basándose en su identificador único.
     *
     * @param idUsuario El ID del usuario que se desea borrar.
     * @return true si la fila fue eliminada con éxito, false en caso contrario.
     */
    @Override
    public boolean eliminar(int idUsuario) {
        Connection con = conexionMySQL.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}