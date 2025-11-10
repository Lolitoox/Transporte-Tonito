package dominio;

import java.time.LocalDate;

public class Camion extends Vehiculo {
    public static class Trailer {
        public enum Tipo { TODO_PUERTA, BARANDA_VOLCABLE, CHATON }
        private String patente;
        private String marca;
        private String modelo;
        private int anio;
        private LocalDate ultimaRtv;
        private int kilometraje;
        private Tipo tipo;

        public Trailer(String patente, String marca, String modelo, int anio, LocalDate ultimaRtv, int kilometraje, Tipo tipo) {
            this.patente = patente.toUpperCase();
            this.marca = marca;
            this.modelo = modelo;
            this.anio = anio;
            this.ultimaRtv = ultimaRtv;
            this.kilometraje = kilometraje;
            this.tipo = tipo;
        }

        public Tipo getTipo() { return tipo; }

        @Override
        public String toString() {
            return "Trailer{patente=" + patente + ", tipo=" + tipo + ", km=" + kilometraje + "}";
        }
    }

    private Trailer trailer; // opcional

    public Camion(String patente, String marca, String modelo, int anio, LocalDate ultimaRtv, int kilometraje, double capacidadCargaKg, Trailer trailer) {
        super(patente, marca, modelo, anio, ultimaRtv, kilometraje, capacidadCargaKg);
        this.trailer = trailer;
    }

    public Trailer getTrailer() { return trailer; }

    @Override
    public String tipo() { return "Camion"; }

    @Override
    public boolean validar() {
        System.out.println("Validando Camión: " + this);
        if (patente.isBlank() || marca.isBlank() || modelo.isBlank()) {
            System.out.println(" - Datos básicos inválidos");
            return false;
        }
        System.out.println(" - OK (trailer=" + (trailer==null?"no":"sí") + ")");
        return true;
    }
}