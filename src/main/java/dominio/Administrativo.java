package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Administrativo extends Empleado {
    private String puesto;
    private char categoria; // 'A', 'B', 'C', ...
    private final List<Empleado> subordinados = new ArrayList<>();

    public Administrativo(int legajo, String dni, String nombre, String apellido, LocalDate fechaNacimiento, int antiguedad, String puesto, char categoria) {
        super(legajo, dni, nombre, apellido, fechaNacimiento, antiguedad);
        this.puesto = puesto == null ? "" : puesto.trim();
        this.categoria = Character.toUpperCase(categoria);
    }

    @Override
    public String tipo() { return "Administrativo"; }

    public String getPuesto() { return puesto; }
    public char getCategoria() { return categoria; }
    public List<Empleado> getSubordinados() { return Collections.unmodifiableList(subordinados); }

    public void agregarSubordinado(Empleado e) {
        if (e == null) return;
        if (e == this) throw new IllegalArgumentException("Un empleado no puede ser su propio subordinado");
        if (subordinados.contains(e)) return;
        subordinados.add(e);
        e.setSupervisor(this);
    }

    @Override
    public boolean validar() {
        System.out.println("Validando Administrativo: " + this);
        if (categoria < 'A' || categoria > 'Z') {
            System.out.println(" - Categoría inválida (debe ser A..Z)");
            return false;
        }
        if (puesto.isBlank()) {
            System.out.println(" - Puesto vacío");
            return false;
        }
        System.out.println(" - OK (" + subordinados.size() + " subordinados)");
        return true;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", puesto='" + puesto + '\'' +
                ", categoria=" + categoria +
                ", cantSub=" + subordinados.size() +
                "}";
    }
}