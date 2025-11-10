package dominio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Empleado {
    protected int legajo;
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected LocalDate fechaNacimiento; // aaaa-mm-dd
    protected int antiguedad; // años
    protected Administrativo supervisor; // null si no tiene

    protected Empleado(int legajo, String dni, String nombre, String apellido, LocalDate fechaNacimiento, int antiguedad) {
        this.legajo = legajo;
        this.dni = Objects.requireNonNull(dni);
        this.nombre = Objects.requireNonNull(nombre);
        this.apellido = Objects.requireNonNull(apellido);
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento);
        if (antiguedad < 0) throw new IllegalArgumentException("Antigüedad no puede ser negativa");
        this.antiguedad = antiguedad;
    }

    public abstract String tipo();
    public abstract boolean validar();

    public int getLegajo() { return legajo; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public int getAntiguedad() { return antiguedad; }

    public Administrativo getSupervisor() { return supervisor; }
    public void setSupervisor(Administrativo supervisor) { this.supervisor = supervisor; }

    protected String baseToString() {
        return "legajo=" + legajo +
               ", dni='" + dni + '\'' +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", nacimiento=" + fechaNacimiento.format(DateTimeFormatter.ISO_LOCAL_DATE) +
               ", antiguedad=" + antiguedad + "a";
    }

    @Override
    public String toString() {
        return tipo() + "{" + baseToString() + "}";
    }
}