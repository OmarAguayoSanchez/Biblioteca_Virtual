package a.biblioteca_virtual.Model;

/**
 * Representa un Libro dentro del sistema de la Biblioteca Virtual.
 * Esta clase modelo encapsula la información bibliográfica de cada ejemplar,
 * así como su disponibilidad en el inventario y sus relaciones con autores y editoriales.
 * * @author Sánchez Cuellar Danna Paola
 * @version 1.0
 */
public class Libro {

    /** Atributos */
    private int idLibro;
    private String titulo;
    private String isbn;
    private int anioPublicacion;
    private String genero;
    private int cantidadDisponible;
    private int idEditorial;
    private int idAutor;

    /**
     * Constructor por defecto.
     */
    public Libro() {
    }

    /**
     * Constructor parametrizado.
     * Crea una instancia de Libro inicializando todos sus atributos principales.
     *
     * @param idLibro            El identificador único del libro.
     * @param titulo             El título oficial de la obra.
     * @param isbn               El código ISBN del libro.
     * @param anioPublicacion    El año en que se publicó la obra.
     * @param genero             El género literario del libro.
     * @param cantidadDisponible El número de copias disponibles para préstamo.
     * @param idEditorial        El ID de la editorial asociada.
     * @param idAutor            El ID del autor asociado.
     */
    public Libro(int idLibro, String titulo, String isbn, int anioPublicacion, String genero, int cantidadDisponible, int idEditorial, int idAutor) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.cantidadDisponible = cantidadDisponible;
        this.idEditorial = idEditorial;
        this.idAutor = idAutor;
    }

    /**
     * Obtiene el identificador único del libro.
     *
     * @return El ID del libro.
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     * Establece el identificador único del libro.
     *
     * @param idLibro El nuevo ID a asignar.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return El título de la obra.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo El nuevo título a asignar.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el código ISBN del libro.
     *
     * @return El ISBN de la obra.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el código ISBN del libro.
     *
     * @param isbn El nuevo ISBN a asignar.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtiene el año de publicación del libro.
     *
     * @return El año de publicación.
     */
    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    /**
     * Establece el año de publicación del libro.
     *
     * @param anioPublicacion El nuevo año de publicación a asignar.
     */
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    /**
     * Obtiene el género literario del libro.
     *
     * @return El género del libro.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Establece el género literario del libro.
     *
     * @param genero El nuevo género a asignar.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene la cantidad de copias disponibles en el inventario.
     *
     * @return La cantidad de ejemplares disponibles para préstamo.
     */
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    /**
     * Establece la cantidad de copias disponibles en el inventario.
     *
     * @param cantidadDisponible La nueva cantidad de ejemplares a asignar.
     */
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    /**
     * Obtiene el identificador de la editorial que publicó el libro.
     *
     * @return El ID de la editorial (Llave Foránea).
     */
    public int getIdEditorial() {
        return idEditorial;
    }

    /**
     * Establece el identificador de la editorial asociada.
     *
     * @param idEditorial El nuevo ID de editorial a asignar.
     */
    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    /**
     * Obtiene el identificador del autor del libro.
     *
     * @return El ID del autor (Llave Foránea).
     */
    public int getIdAutor() {
        return idAutor;
    }

    /**
     * Establece el identificador del autor asociado.
     *
     * @param idAutor El nuevo ID de autor a asignar.
     */
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
}