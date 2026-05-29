package a.biblioteca_virtual.Model;

import java.time.LocalDate;

/**
 * Representa el detalle de un préstamo en el sistema de la Biblioteca Virtual.
 * Esta clase modelo encapsula la información de cada libro individual que pertenece
 * a una transacción de préstamo (cabecera), gestionando sus fechas de devolución y estado.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class DetallePrestamo {

    /** Atributos */
    private int idDetalle;
    private int idPrestamo;
    private int idLibro;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado;

    // --- Atributos de Vista  ---

    /** Nombre del usuario que realizó el préstamo. Utilizado para mostrar en tablas de la interfaz gráfica. */
    private String nombreUsuario;

    /** Título del libro prestado. Utilizado para mostrar en tablas de la interfaz gráfica. */
    private String tituloLibro;

    /** Fecha original en la que se realizó el préstamo. Utilizado para mostrar en tablas de la interfaz gráfica. */
    private LocalDate fechaPrestamo;

    /**
     * Constructor por defecto.
     */
    public DetallePrestamo() {
    }

    /**
     * Constructor parametrizado para los datos base de la tabla.
     *
     * @param idDetalle               El identificador único del detalle.
     * @param idPrestamo              El identificador del préstamo general.
     * @param idLibro                 El identificador del libro prestado.
     * @param fechaDevolucionEsperada La fecha límite de entrega.
     * @param fechaDevolucionReal     La fecha real de entrega (puede ser null).
     * @param estado                  El estado actual de este detalle.
     */
    public DetallePrestamo(int idDetalle, int idPrestamo, int idLibro, LocalDate fechaDevolucionEsperada, LocalDate fechaDevolucionReal, String estado) {
        this.idDetalle = idDetalle;
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único del detalle.
     *
     * @return El ID del detalle.
     */
    public int getIdDetalle() {
        return idDetalle;
    }

    /**
     * Establece el identificador único del detalle.
     *
     * @param idDetalle El nuevo ID a asignar.
     */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Obtiene el identificador del préstamo (cabecera) asociado.
     *
     * @return El ID del préstamo.
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Establece el identificador del préstamo asociado.
     *
     * @param idPrestamo El nuevo ID de préstamo a asignar.
     */
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    /**
     * Obtiene el identificador del libro prestado.
     *
     * @return El ID del libro.
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     * Establece el identificador del libro prestado.
     *
     * @param idLibro El nuevo ID de libro a asignar.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     * Obtiene la fecha límite esperada para la devolución del libro.
     *
     * @return La fecha de devolución esperada.
     */
    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    /**
     * Establece la fecha límite esperada para la devolución.
     *
     * @param fechaDevolucionEsperada La nueva fecha esperada a asignar.
     */
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    /**
     * Obtiene la fecha real en la que se entregó el libro.
     *
     * @return La fecha de devolución real, o null si aún no ha sido devuelto.
     */
    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    /**
     * Establece la fecha real en la que el usuario devolvió el libro.
     *
     * @param fechaDevolucionReal La fecha de devolución real a asignar.
     */
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    /**
     * Obtiene el nombre del usuario asociado al préstamo.
     * Este dato es obtenido mediante un cruce de tablas (JOIN).
     *
     * @return El nombre del usuario.
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre del usuario asociado al préstamo.
     *
     * @param nombreUsuario El nombre del usuario a asignar.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene el título del libro prestado.
     * Este dato es obtenido mediante un cruce de tablas (JOIN).
     *
     * @return El título del libro.
     */
    public String getTituloLibro() {
        return tituloLibro;
    }

    /**
     * Establece el título del libro prestado.
     *
     * @param tituloLibro El título del libro a asignar.
     */
    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    /**
     * Obtiene la fecha en la que se realizó la transacción original del préstamo.
     * Este dato proviene de la tabla cabecera mediante JOIN.
     *
     * @return La fecha del préstamo original.
     */
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Establece la fecha de la transacción original del préstamo.
     *
     * @param fechaPrestamo La fecha de préstamo a asignar.
     */
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     * Obtiene el estado actual de este registro de préstamo.
     *
     * @return El estado (ej. "Activo", "Devuelto").
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de este registro de préstamo.
     *
     * @param estado El nuevo estado a asignar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}