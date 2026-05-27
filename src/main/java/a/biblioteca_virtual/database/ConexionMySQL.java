package a.biblioteca_virtual.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos MySQL usando el patrón de diseño Singleton.
 * Garantiza que solo exista una única instancia de la conexión en toda la aplicación,
 * optimizando el uso de recursos y evitando múltiples conexiones simultáneas.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class ConexionMySQL {

    // --- CONSTANTES DE CONEXIÓN ---
    private static final String URL = "jdbc:mysql://localhost:3306/Biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "Plebada";

    /**
     * Instancia única de la clase (Singleton).
     * Se utiliza 'volatile' para asegurar que los cambios en esta variable
     * sean visibles inmediatamente para todas las ejecuciónes.
     */
    private static volatile ConexionMySQL instancia;

    /**
     * Objeto de conexión.
     */
    private Connection conexion;

    /**
     * Constructor privado para evitar la instanciación desde otras clases.
     * Inicializa el controlador y establece la conexión con la base de datos.
     */
    private ConexionMySQL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL establecida con éxito.");

        } catch (ClassNotFoundException e) {
            System.err.println("Error crítico: No se encontró el Driver de MySQL. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    }

    /**
     * Devuelve la instancia única de la clase ConexionMySQL.
     *
     * @return La única instancia activa de ConexionMySQL.
     */
    public static ConexionMySQL getInstancia() {
        if (instancia == null) {
            // Bloque sincronizado para evitar que dos hilos creen instancias simultáneas
            synchronized (ConexionMySQL.class) {
                if (instancia == null) {
                    instancia = new ConexionMySQL();
                }
            }
        }
        return instancia;
    }

    /**
     * Permite acceder al objeto de conexión para realizar consultas u operaciones.
     *
     * @return El objeto Connection activo.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Método auxiliar estático para mantener compatibilidad directa con los DAOs.
     * Evita tener que escribir ConexionMySQL.getInstancia().getConexion() en cada consulta.
     * @return El objeto Connection activo.
     */
    public static Connection getConnection() {
        return getInstancia().getConexion();
    }

    /**
     * Cierra la conexión activa con la base de datos de manera segura.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada de forma segura.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar cerrar la conexión: " + e.getMessage());
        }
    }
}