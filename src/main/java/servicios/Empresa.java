package servicios;

import dominio.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class Empresa {
    private final Map<Integer, Empleado> empleadosPorLegajo = new HashMap<>();
    private final Map<String, Vehiculo> vehiculosPorPatente = new HashMap<>();
    private final List<Viaje> viajes = new ArrayList<>();

    // --- Empleados ---
    public boolean agregarAdministrativo(Administrativo a) {
        if (a == null) return false;
        if (empleadosPorLegajo.containsKey(a.getLegajo())) {
            System.out.println("Ya existe empleado con legajo " + a.getLegajo());
            return false;
        }
        if (!a.validar()) return false;
        empleadosPorLegajo.put(a.getLegajo(), a);
        return true;
    }

    public boolean agregarChofer(Chofer c) {
        if (c == null) return false;
        if (empleadosPorLegajo.containsKey(c.getLegajo())) {
            System.out.println("Ya existe empleado con legajo " + c.getLegajo());
            return false;
        }
        if (!c.validar()) return false;
        empleadosPorLegajo.put(c.getLegajo(), c);
        return true;
    }

    public Empleado buscarEmpleado(int legajo) {
        return empleadosPorLegajo.get(legajo);
    }

    // Subordinados (solo administrativos con categoría E o superior)
    public boolean agregarSubordinado(int legajoSupervisor, int legajoSubordinado) {
        Empleado sup = empleadosPorLegajo.get(legajoSupervisor);
        Empleado sub = empleadosPorLegajo.get(legajoSubordinado);
        if (!(sup instanceof Administrativo)) {
            System.out.println("El supervisor no es administrativo");
            return false;
        }
        Administrativo a = (Administrativo) sup;
        if (a.getCategoria() < 'E') {
            System.out.println("El administrativo no tiene categoría E o superior");
            return false;
        }
        if (sub == null) {
            System.out.println("Subordinado inexistente");
            return false;
        }
        if (sub.getSupervisor() != null && sub.getSupervisor() != a) {
            System.out.println("El empleado ya tiene otro supervisor");
            return false;
        }
        // Evitar ciclos: verificar si 'a' está bajo 'sub' en cadena
        if (creaCiclo(a, sub)) {
            System.out.println("No se puede crear ciclo jerárquico (supervisor por debajo de su subordinado)");
            return false;
        }
        a.agregarSubordinado(sub);
        return true;
    }

    private boolean creaCiclo(Administrativo supervisor, Empleado subordinado) {
        // Chequear ascendencia de supervisor en árbol de 'subordinado'
        Empleado cursor = subordinado;
        while (cursor != null) {
            if (cursor == supervisor) return true;
            cursor = cursor.getSupervisor();
        }
        return false;
    }

    // --- Vehículos ---
    public boolean agregarVehiculo(Vehiculo v) {
        if (v == null) return false;
        String pat = v.getPatente();
        if (vehiculosPorPatente.containsKey(pat)) {
            System.out.println("Ya existe vehículo con patente " + pat);
            return false;
        }
        if (!v.validar()) return false;
        vehiculosPorPatente.put(pat, v);
        return true;
    }

    public Vehiculo buscarVehiculo(String patente) {
        return vehiculosPorPatente.get(patente.toUpperCase());
    }

    // --- Viajes ---
    public boolean agregarViaje(int legajoChofer, String origen, String destino, int km, String patenteVehiculo) {
        Empleado e = empleadosPorLegajo.get(legajoChofer);
        if (!(e instanceof Chofer)) {
            System.out.println("El legajo indicado no corresponde a un chofer");
            return false;
        }
        Chofer chofer = (Chofer) e;
        Vehiculo vehiculo = vehiculosPorPatente.get(patenteVehiculo.toUpperCase());
        if (vehiculo == null) {
            System.out.println("Vehículo inexistente");
            return false;
        }
        if (!habilitaConCarnet(chofer, vehiculo)) {
            System.out.println("El carnet del chofer no habilita ese vehículo");
            return false;
        }
        // Validaciones de service y RTV
        LocalDate hoy = LocalDate.now();
        boolean requiereService = vehiculo.requiereService();
        boolean requiereRtv = vehiculo.requiereRTV(hoy);

        if (requiereService) {
            System.out.println("ATENCIÓN: el vehículo requiere SERVICE (cada 10.000 km). Último a los " + vehiculo.getUltimoServiceKm() + " km.");
        }
        if (requiereRtv) {
            System.out.println("ATENCIÓN: el vehículo requiere RTV (anual). Última: " + vehiculo.getUltimaRtv());
        }

        Viaje viaje = new Viaje(chofer, origen, destino, km, vehiculo);
        viajes.add(viaje);
        vehiculo.sumarKm(km);
        return true;
    }

    private boolean habilitaConCarnet(Chofer ch, Vehiculo v) {
        Carnet.Tipo t = ch.getCarnet().getTipo();
        if (v instanceof Camioneta) {
            return t == Carnet.Tipo.B || t == Carnet.Tipo.C || t == Carnet.Tipo.E;
        }
        if (v instanceof Camion) {
            Camion cam = (Camion) v;
            if (cam.getTrailer() == null) {
                return t == Carnet.Tipo.C || t == Carnet.Tipo.E;
            } else {
                return t == Carnet.Tipo.E;
            }
        }
        return false;
    }

    public List<Viaje> viajesPorLegajo(int legajoChofer) {
        Empleado e = empleadosPorLegajo.get(legajoChofer);
        if (!(e instanceof Chofer)) {
            System.out.println("El legajo no corresponde a un chofer.");
            return Collections.emptyList();
        }
        List<Viaje> res = new ArrayList<>();
        for (Viaje v : viajes) {
            if (v.getChofer().getLegajo() == legajoChofer) res.add(v);
        }
        return res;
    }

    public List<Viaje> viajesPorPatente(String patente) {
        String pat = patente.toUpperCase();
        List<Viaje> res = new ArrayList<>();
        for (Viaje v : viajes) {
            if (v.getVehiculo().getPatente().equalsIgnoreCase(pat)) res.add(v);
        }
        return res;
    }

    public Map<Empleado, Double> salarios() {
        Map<Empleado, Double> res = new LinkedHashMap<>();
        // precomputar km por chofer
        Map<Integer, Integer> kmChofer = new HashMap<>();
        for (Viaje v : viajes) {
            kmChofer.merge(v.getChofer().getLegajo(), v.getKm(), Integer::sum);
        }
        for (Empleado e : empleadosPorLegajo.values()) {
            if (e instanceof Administrativo) {
                Administrativo a = (Administrativo) e;
                int delta = Math.max(0, (int)a.getCategoria() - (int)'A');
                double sueldo = 1000 + delta * 150;
                res.put(e, sueldo);
            } else if (e instanceof Chofer) {
                int km = kmChofer.getOrDefault(e.getLegajo(), 0);
                double sueldo = 800 + km * 0.10;
                res.put(e, sueldo);
            }
        }
        return res;
    }

    public Collection<Empleado> empleados() { return empleadosPorLegajo.values(); }
    public Collection<Vehiculo> vehiculos() { return vehiculosPorPatente.values(); }
    public List<Viaje> getViajes() { return Collections.unmodifiableList(viajes); }
}