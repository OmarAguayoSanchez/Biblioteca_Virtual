package a.biblioteca_virtual.Model;

import java.time.LocalDate;

public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private LocalDate fechaPrestamo;

    public Prestamo() {
    }

    public Prestamo(int idPrestamo, int idUsuario, LocalDate fechaPrestamo) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = fechaPrestamo;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
}
