package a.biblioteca_virtual.dao;

import a.biblioteca_virtual.database.ConexionMySQL;
import a.biblioteca_virtual.seguridad.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación del DAO para la entidad Usuario usando MySQL.
 * Gestiona la autenticación (login) y el registro de nuevos usuarios,
 * asegurando que las contraseñas se manejen mediante hashes.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class UsuarioDAOMySQL implements IUsuarioDAO {

    // --- CONSTANTES SQL ---
    private static final String SQL_SELECT_ROLE =
            "SELECT rol FROM usuarios WHERE username = ? AND password_hash = ?";

    private static final String SQL_INSERT_USER =
            "INSERT INTO usuarios (nombre_completo, correo, username, password_hash, rol) VALUES (?, ?, ?, ?, ?)";

    // --- CONSTANTES DE ERRORES ---
    /** Código de error de MySQL devuelto cuando se viola una restricción UNIQUE (ej. correo duplicado) */
    private static final int MYSQL_ERR_DUPLICATE_ENTRY = 1062;

    /**
     * Conexión principal a la base de datos MySQL (Singleton).
     */
    private final Connection conexion;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     */
    public UsuarioDAOMySQL() {
        this.conexion = ConexionMySQL.getInstancia().getConexion();
    }

    /**
     * Verifica las credenciales de un usuario.
     * Convierte la contraseña plana sin hash
     * antes de consultar la base de datos.
     *
     * @param username      El nombre de usuario ingresado.
     * @param passwordPlana La contraseña original escrita por el usuario.
     * @return El rol del usuario si es válido, o null si las credenciales fallan.
     */
    @Override
    public String autenticar(String username, String passwordPlana) {
        String hashCalculado = HashUtil.generarSHA256(passwordPlana);

        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ROLE)) {
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
     * @param passwordPlana  La contraseña elegida por el usuario.
     * @param rol            El nivel de permisos.
     * @return true si se registró exitosamente, false si ocurrió un error.
     */
    @Override
    public boolean registrar(String nombreCompleto, String correo, String username, String passwordPlana, String rol) {
        String hashParaGuardar = HashUtil.generarSHA256(passwordPlana);

        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_USER)) {
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
}