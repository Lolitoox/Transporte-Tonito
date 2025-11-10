package dominio;

import java.time.LocalDate;

public class Chofer extends Empleado {
    private Carnet carnet;

    public Chofer(int legajo, String dni, String nombre, String apellido, LocalDate fechaNacimiento, int antiguedad, Carnet carnet) {
        super(legajo, dni, nombre, apellido, fechaNacimiento, antiguedad);
        this.carnet = carnet;
    }

    public Carnet getCarnet() { return carnet; }

    @Override
    public String tipo() { return "Chofer"; }

    @Override
    public boolean validar() {
        System.out.println("Validando Chofer: " + this);
        if (carnet == null) {
            System.out.println(" - Carnet nulo");
            return false;
        }
        System.out.println(" - OK (" + carnet + ")");
        return true;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", carnet=" + carnet + "}";
    }
}