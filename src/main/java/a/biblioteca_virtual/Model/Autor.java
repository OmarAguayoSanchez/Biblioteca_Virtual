package a.biblioteca_virtual.Model;

import java.time.LocalDate;

<<<<<<< HEAD
/**
 * Representa un Autor dentro del sistema de la Biblioteca Virtual.
 * Esta clase modelo encapsula la información personal de los escritores
 * cuyos libros están registrados en el catálogo.
 * * @author TuNombre
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
=======
public class Autor {
    private int idAutor;
    private String nombre;
    private String nacionalidad;
    private LocalDate fechaNacimiento;

    public Autor() {
    }

>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public Autor(int idAutor, String nombre, String nacionalidad, LocalDate fechaNacimiento) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
    }

<<<<<<< HEAD
    /**
     * Obtiene el identificador único del autor.
     *
     * @return El ID del autor.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public int getIdAutor() {
        return idAutor;
    }

<<<<<<< HEAD
    /**
     * Establece el identificador único del autor.
     *
     * @param idAutor El nuevo ID a asignar.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

<<<<<<< HEAD
    /**
     * Obtiene el nombre completo del autor.
     *
     * @return El nombre del autor.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public String getNombre() {
        return nombre;
    }

<<<<<<< HEAD
    /**
     * Establece el nombre completo del autor.
     *
     * @param nombre El nuevo nombre a asignar.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

<<<<<<< HEAD
    /**
     * Obtiene la nacionalidad del autor.
     *
     * @return La nacionalidad del autor.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public String getNacionalidad() {
        return nacionalidad;
    }

<<<<<<< HEAD
    /**
     * Establece la nacionalidad del autor.
     *
     * @param nacionalidad La nueva nacionalidad a asignar.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

<<<<<<< HEAD
    /**
     * Obtiene la fecha de nacimiento del autor.
     *
     * @return La fecha de nacimiento.
     */
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

<<<<<<< HEAD
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
=======
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
}