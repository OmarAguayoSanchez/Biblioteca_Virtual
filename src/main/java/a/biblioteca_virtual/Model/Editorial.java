package a.biblioteca_virtual.Model;

/**
 * Representa una Editorial dentro del sistema de la Biblioteca Virtual.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class Editorial {

    /** Atributos */
    private int idEditorial;
    private String nombre;
    private String pais;
    private String telefono;

    /**
     * Constructor por defecto.
     */
    public Editorial() {
    }

    /**
     * Constructor parametrizado.
     * Crea una instancia de Editorial inicializando todos sus atributos.
     *
     * @param idEditorial El identificador único de la editorial.
     * @param nombre      El nombre oficial de la editorial.
     * @param pais        El país de origen de la editorial.
     * @param telefono    El número de teléfono de contacto.
     */
    public Editorial(int idEditorial, String nombre, String pais, String telefono) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
        this.pais = pais;
        this.telefono = telefono;
    }

    /**
     * Obtiene el identificador único de la editorial.
     *
     * @return El ID de la editorial.
     */
    public int getIdEditorial() {
        return idEditorial;
    }

    /**
     * Establece el identificador único de la editorial.
     *
     * @param idEditorial El nuevo ID a asignar.
     */
    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    /**
     * Obtiene el nombre de la editorial.
     *
     * @return El nombre de la casa editorial.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la editorial.
     *
     * @param nombre El nuevo nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el país de origen de la editorial.
     *
     * @return El país de la editorial.
     */
    public String getPais() {
        return pais;
    }

    /**
     * Establece el país de origen de la editorial.
     *
     * @param pais El nuevo país a asignar.
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * Obtiene el teléfono de contacto de la editorial.
     *
     * @return El número de teléfono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto de la editorial.
     *
     * @param telefono El nuevo teléfono a asignar.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve el nombre en formato de cadena de texto del objeto Editorial.
     *
     * @return El nombre de la editorial.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}
