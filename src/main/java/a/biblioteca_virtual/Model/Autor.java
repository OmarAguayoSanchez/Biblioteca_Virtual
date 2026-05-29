package a.biblioteca_virtual.Model;

import java.time.LocalDate;

/**
 * Representa un Autor dentro del sistema de la Biblioteca Virtual.
 * Esta clase modelo encapsula la información personal de los escritores
 * cuyos libros están registrados en el catálogo.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class Autor {

    /** Getters y setter. */
    private int idAutor;

    private String nombre;

    private String nacionalidad;

    private LocalDate fechaNacimiento;

    /**
     * Constructor por defecto.
     */
    public Autor() {
    }

    /**
     * Constructor parametrizado.
     * Crea una instancia de Autor inicializando todos sus atributos.
     *
     * @param idAutor         El identificador único del autor.
     * @param nombre          El nombre completo del autor.
     * @param nacionalidad    El país de origen del autor.
     * @param fechaNacimiento La fecha de nacimiento del autor usando {@link LocalDate}.
     */
    public Autor(int idAutor, String nombre, String nacionalidad, LocalDate fechaNacimiento) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene el identificador único del autor.
     *
     * @return El ID del autor.
     */
    public int getIdAutor() {
        return idAutor;
    }

    /**
     * Establece el identificador único del autor.
     *
     * @param idAutor El nuevo ID a asignar.
     */
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    /**
     * Obtiene el nombre completo del autor.
     *
     * @return El nombre del autor.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo del autor.
     *
     * @param nombre El nuevo nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la nacionalidad del autor.
     *
     * @return La nacionalidad del autor.
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del autor.
     *
     * @param nacionalidad La nueva nacionalidad a asignar.
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene la fecha de nacimiento del autor.
     *
     * @return La fecha de nacimiento.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del autor.
     *
     * @param fechaNacimiento La nueva fecha de nacimiento a asignar.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve el nombre en formato de cadena de texto del objeto Autor.
     *
     * @return El nombre completo del autor.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}