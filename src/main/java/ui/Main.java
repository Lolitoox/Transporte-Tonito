package ui;
import dominio.*;
import servicios.Empresa;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Empresa empresa = new Empresa();

    public static void main(String[] args) {
        int opcion;
        do {
            menu();
            opcion = leerEntero("Elija una opción: ", 0, 6);
            switch (opcion) {
                case 1 -> cargarEmpleado();
                case 2 -> cargarSubordinados();
                case 3 -> cargarVehiculo();
                case 4 -> cargarViaje();
                case 5 -> listadoViajes();
                case 6 -> salarios();
                case 0 -> System.out.println("Saliendo...");
            }
            System.out.println();
        } while (opcion != 0);
    }

    private static void menu() {
        System.out.println("""
                ===== Transporte Tonito =====
                1) Cargar Empleado
                2) Cargar Subordinados
                3) Cargar Vehículos
                4) Cargar Viaje
                5) Listado de viajes
                6) Salario de empleados
                0) Salir
                """);
    }

    // --- Helpers de lectura ---
    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try { return Integer.parseInt(s); } catch (Exception e) { System.out.println("Ingrese un entero válido."); }
        }
    }
    private static int leerEntero(String prompt, int min, int max) {
        while (true) {
            int v = leerEntero(prompt);
            if (v < min || v > max) {
                System.out.println("Debe estar entre " + min + " y " + max);
            } else return v;
        }
    }
    private static double leerDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(s.replace(',', '.'));
                if (v < min || v > max) System.out.println("Debe estar entre " + min + " y " + max);
                else return v;
            } catch (Exception e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
    private static String leerNoVacio(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("No puede estar vacío.");
        }
    }
    private static LocalDate leerFechaISO(String prompt) {
        while (true) {
            System.out.print(prompt + " (aaaa-mm-dd): ");
            String s = sc.nextLine().trim();
            try { return LocalDate.parse(s); } catch (Exception e) { System.out.println("Formato inválido. Ej: 1990-05-23"); }
        }
    }
    private static Year leerAnio(String prompt) {
        while (true) {
            int a = leerEntero(prompt);
            if (a >= 1900 && a <= 2100) return Year.of(a);
            System.out.println("Año fuera de rango (1900..2100).");
        }
    }

    // --- Cargar Empleado ---
    private static void cargarEmpleado() {
        System.out.println("1) Administrativo | 2) Chofer");
        int t = leerEntero("Tipo: ", 1, 2);
        int legajo = leerEntero("Legajo (entero): ");
        String dni = leerNoVacio("DNI: ");
        String nombre = leerNoVacio("Nombre: ");
        String apellido = leerNoVacio("Apellido: ");
        LocalDate fn = leerFechaISO("Fecha de nacimiento");
        int antig = leerEntero("Antigüedad (años): ", 0, 80);

        if (t == 1) {
            String puesto = leerNoVacio("Puesto: ");
            char categoria;
            while (true) {
                String s = leerNoVacio("Categoría (A..Z): ").toUpperCase();
                if (s.length() == 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') { categoria = s.charAt(0); break; }
                System.out.println("Debe ser una sola letra entre A y Z.");
            }
            Administrativo a = new Administrativo(legajo, dni, nombre, apellido, fn, antig, puesto, categoria);
            if (empresa.agregarAdministrativo(a)) System.out.println("Administrativo agregado.");
        } else {
            System.out.println("Carnet habilitante:");
            Carnet.Tipo tipoCarnet = leerTipoCarnet();
            String letra = leerNoVacio("Letra: ");
            int numero = leerEntero("Número: ");
            Year venc = leerAnio("Año de vencimiento: ");
            Carnet carnet = new Carnet(tipoCarnet, letra, numero, venc);
            if (!carnet.vigente(Year.now())) System.out.println("ADVERTENCIA: Carnet vencido.");

            Chofer c = new Chofer(legajo, dni, nombre, apellido, fn, antig, carnet);
            if (empresa.agregarChofer(c)) System.out.println("Chofer agregado.");
        }
    }

    private static Carnet.Tipo leerTipoCarnet() {
        while (true) {
            String s = leerNoVacio("Tipo (B/C/E): ").toUpperCase();
            switch (s) {
                case "B": return Carnet.Tipo.B;
                case "C": return Carnet.Tipo.C;
                case "E": return Carnet.Tipo.E;
                default: System.out.println("Sólo B, C o E.");
            }
        }
    }

    // --- Cargar Subordinados ---
    private static void cargarSubordinados() {
        int legSup = leerEntero("Legajo del supervisor (administrativo E o superior): ");
        int legSub = leerEntero("Legajo del subordinado: ");
        if (empresa.agregarSubordinado(legSup, legSub)) System.out.println("Subordinado asignado.");
    }

    // --- Cargar Vehículo ---
    private static void cargarVehiculo() {
        System.out.println("1) Camión | 2) Camioneta");
        int t = leerEntero("Tipo: ", 1, 2);
        String patente = leerNoVacio("Patente: ").toUpperCase();
        String marca = leerNoVacio("Marca: ");
        String modelo = leerNoVacio("Modelo: ");
        int anio = leerEntero("Año: ", 1950, 2100);
        LocalDate rtv = leerFechaISO("Última RTV");
        int km = leerEntero("Kilometraje actual: ", 0, 2_000_000);
        double carga = leerDouble("Capacidad de carga (kg): ", 0, 100_000);

        if (t == 1) {
            System.out.print("¿Tiene tráiler? (s/n): ");
            String s = sc.nextLine().trim().toLowerCase();
            Camion.Trailer trailer = null;
            if (s.startsWith("s")) {
                String p2 = leerNoVacio("Patente tráiler: ").toUpperCase();
                String m2 = leerNoVacio("Marca tráiler: ");
                String mo2 = leerNoVacio("Modelo tráiler: ");
                int a2 = leerEntero("Año tráiler: ", 1950, 2100);
                LocalDate r2 = leerFechaISO("Última RTV tráiler");
                int k2 = leerEntero("Kilometraje tráiler: ", 0, 2_000_000);
                Camion.Trailer.Tipo tt = leerTipoTrailer();
                trailer = new Camion.Trailer(p2, m2, mo2, a2, r2, k2, tt);
            }
            Camion cam = new Camion(patente, marca, modelo, anio, rtv, km, carga, trailer);
            if (empresa.agregarVehiculo(cam)) System.out.println("Camión agregado.");
        } else {
            Camioneta.TipoTransporte tipo = leerTipoTransporte();
            Camioneta van = new Camioneta(patente, marca, modelo, anio, rtv, km, carga, tipo);
            if (empresa.agregarVehiculo(van)) System.out.println("Camioneta agregada.");
        }
    }

    private static Camion.Trailer.Tipo leerTipoTrailer() {
        while (true) {
            System.out.print("Tipo tráiler (1:TODO_PUERTA, 2:BARANDA_VOLCABLE, 3:CHATON): ");
            String s = sc.nextLine().trim();
            switch (s) {
                case "1": return Camion.Trailer.Tipo.TODO_PUERTA;
                case "2": return Camion.Trailer.Tipo.BARANDA_VOLCABLE;
                case "3": return Camion.Trailer.Tipo.CHATON;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    private static Camioneta.TipoTransporte leerTipoTransporte() {
        while (true) {
            System.out.print("Tipo transporte (1:PASAJEROS, 2:MERCADERIA): ");
            String s = sc.nextLine().trim();
            switch (s) {
                case "1": return Camioneta.TipoTransporte.PASAJEROS;
                case "2": return Camioneta.TipoTransporte.MERCADERIA;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    // --- Cargar Viaje ---
    private static void cargarViaje() {
        int leg = leerEntero("Legajo chofer: ");
        String ori = leerNoVacio("Origen: ");
        String des = leerNoVacio("Destino: ");
        int km = leerEntero("Kilómetros realizados: ", 1, 50_000);
        String pat = leerNoVacio("Patente vehículo: ").toUpperCase();

        if (empresa.agregarViaje(leg, ori, des, km, pat)) {
            System.out.println("Viaje registrado.");
        }
    }

    // --- Listado de viajes ---
    private static void listadoViajes() {
        System.out.println("Listar por: 1) Legajo (sólo choferes) | 2) Patente");
        int t = leerEntero("Tipo: ", 1, 2);
        List<Viaje> lista;
        if (t == 1) {
            int leg = leerEntero("Legajo chofer: ");
            lista = empresa.viajesPorLegajo(leg);
        } else {
            String pat = leerNoVacio("Patente: ").toUpperCase();
            lista = empresa.viajesPorPatente(pat);
        }
        if (lista.isEmpty()) {
            System.out.println("Sin viajes para el criterio dado.");
            return;
        }
        System.out.println("PATENTE | LEGAJO | DESDE -> HASTA | KM");
        for (Viaje v : lista) {
            System.out.println(v);
        }
    }

    // --- Salarios ---
    private static void salarios() {
        Map<Empleado, Double> sal = empresa.salarios();
        if (sal.isEmpty()) {
            System.out.println("No hay empleados cargados.");
            return;
        }
        System.out.println("Legajo | Nombre | Tipo | Salario");
        for (Map.Entry<Empleado, Double> e : sal.entrySet()) {
            Empleado emp = e.getKey();
            System.out.printf(Locale.US, "%d | %s %s | %s | $%.2f%n",
                    emp.getLegajo(), emp.getNombre(), emp.getApellido(), emp.tipo(), e.getValue());
        }
    }
}