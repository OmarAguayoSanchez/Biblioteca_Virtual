package a.biblioteca_virtual.Model;

public class Editorial {
    private int idEditorial;
    private String nombre;
    private String pais;
    private String telefono;

    public Editorial() {
    }

    public Editorial(int idEditorial, String nombre, String pais, String telefono) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
        this.pais = pais;
        this.telefono = telefono;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
<<<<<<< HEAD
    @Override
    public String toString() {
        return this.nombre;
    }
=======
>>>>>>> a2165958174cb39afffd8e8bb15b63f6f1755d98
}
