package a.biblioteca_virtual.Model;

import java.time.LocalDate;

/**
 * Representa la cabecera de un Préstamo en el sistema de la Biblioteca Virtual.
 * Esta clase modelo encapsula la información general de una transacción,
 * vinculando a un usuario específico con la fecha en que realizó el movimiento.
 * Los libros individuales asociados a este préstamo se gestionan a través
 * de la clase {@link DetallePrestamo}.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class Prestamo {

    /** Atributo */
    private int idPrestamo;
    private int idUsuario;
    private LocalDate fechaPrestamo;

    /**
     * Constructor por defecto.
     */
    public Prestamo() {
    }

    /**
     * Constructor parametrizado.
     * Crea una instancia de Prestamo inicializando todos sus atributos.
     *
     * @param idPrestamo    El identificador único de la transacción de préstamo.
     * @param idUsuario     El identificador del usuario que realiza el préstamo.
     * @param fechaPrestamo La fecha en la que se lleva a cabo el préstamo.
     */
    public Prestamo(int idPrestamo, int idUsuario, LocalDate fechaPrestamo) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     * Obtiene el identificador único del préstamo.
     *
     * @return El ID del préstamo.
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Establece el identificador único del préstamo.
     *
     * @param idPrestamo El nuevo ID de préstamo a asignar.
     */
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    /**
     * Obtiene el identificador del usuario que realizó el préstamo.
     *
     * @return El ID del usuario (Llave Foránea).
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario que realiza el préstamo.
     *
     * @param idUsuario El nuevo ID de usuario a asignar.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene la fecha en la que se realizó el préstamo.
     *
     * @return La fecha de la transacción.
     */
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Establece la fecha en la que se realizó el préstamo.
     *
     * @param fechaPrestamo La nueva fecha de transacción a asignar.
     */
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
}