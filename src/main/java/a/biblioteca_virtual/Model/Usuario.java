package a.biblioteca_virtual.Model;

/**
 * Modelo que representa a un usuario dentro del sistema.
 *
 * @author Omar Alejandro Aguayo Sanchez
 */
public class Usuario {

    // --- ATRIBUTOS ---
    private int idUsuario;
    private String nombreCompleto;
    private String correo;
    private String username;
    private String passwordHash;
    private String rol;

    // --- CONSTRUCTORES ---

    public Usuario() {
    }

    /**
     * Constructor para el registro de nuevos usuarios.
     *
     * @param nombreCompleto Nombre real del usuario.
     * @param correo         Dirección de correo electrónico.
     * @param username       Nombre de usuario para el inicio de sesión.
     * @param passwordHash   Contraseña ya procesada mediante un algoritmo de hash.
     * @param rol            Nivel de acceso (ej. "ADMIN" o "CLIENT").
     */
    public Usuario(String nombreCompleto, String correo, String username, String passwordHash, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    /**
     * Constructor completo para la recuperación de datos desde la base de datos.
     *
     * @param idUsuario      Identificador único generado por el sistema.
     * @param nombreCompleto Nombre real del usuario.
     * @param correo         Correo electrónico.
     * @param username       Nombre de usuario.
     * @param passwordHash   Hash de la contraseña almacenado.
     * @param rol            Rol asignado al usuario.
     */
    public Usuario(int idUsuario, String nombreCompleto, String correo, String username, String passwordHash, String rol) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // --- MÉTODOS SOBRESCRITOS ---

    /**
     * Devuelve una representación textual simplificada del usuario.
     *
     * @return Cadena con el nombre de usuario y su rol.
     */
    @Override
    public String toString() {
        return String.format("%s [%s]", username, rol);
    }
}